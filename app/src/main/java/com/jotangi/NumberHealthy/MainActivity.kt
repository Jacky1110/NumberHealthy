package com.jotangi.NumberHealthy

import android.Manifest
import android.animation.ObjectAnimator
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Geocoder
import android.location.Location
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import com.jotangi.NumberHealthy.api.book.BookApiRepository
import com.jotangi.NumberHealthy.api.book.BookViewModel
import com.jotangi.NumberHealthy.databinding.ActivityMainBinding
import com.jotangi.NumberHealthy.ui.mylittlemin.UserLoginRequest
import com.jotangi.NumberHealthy.utils.DateUtil
import com.jotangi.NumberHealthy.utils.DialogUtil
import com.jotangi.NumberHealthy.utils.SharedPreferencesUtil
import com.jotangi.NumberHealthy.utils.smartwatch.WatchUtils
import com.jotangi.NumberHealthy.api.watch.WatchViewModel
import com.yucheng.ycbtsdk.Constants
import com.yucheng.ycbtsdk.YCBTClient
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private val TAG: String = "${javaClass.simpleName}(TAG)"

    private lateinit var binding: ActivityMainBinding
    private val apiRepository: BookApiRepository by lazy { BookApiRepository() }

    private val TAKE_PHOTO: Int = 1
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService
    var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA//当前相机
    var preview: Preview? = null//预览对象
    var videoCapture: VideoCapture? = null//录像用例
    var cameraProvider: ProcessCameraProvider? = null//相机信息
    var camera: Camera? = null//相机对象
    var isShowCamera = false

    val watchViewModel: WatchViewModel by viewModel()
    val bookViewModel: BookViewModel by viewModel()

    private lateinit var outputDirectory: File

    var mMediaPlayer: MediaPlayer? = null

//    val pictureCallback = Camera.PictureCallback { data, camera ->
//        val pictureFile = File(
//            Environment.getExternalStorageDirectory(),
//            "humHealth-" + System.currentTimeMillis() + ".jpg"
//
//        )
//        try {
//            val fos = FileOutputStream(pictureFile)
//            fos.write(data)
//            fos.close();
//        } catch (e: java.lang.Exception) {
//            e.printStackTrace()
//        }
//    }

    companion object {
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private val REQUIRED_PERMISSIONS =
            mutableListOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        initData()
        initView()
        initDeviceToAppCommandReceiver()
    }

    private fun initData() {

        val appVersion = SharedPreferencesUtil.instances.getAppVersion()
        val versionCode = BuildConfig.VERSION_CODE
        if (appVersion.isNullOrBlank() || appVersion.toInt() < BuildConfig.VERSION_CODE) {
            SharedPreferencesUtil.instances.setAppVersion(versionCode.toString())
        }

//        CrashManager.getInstance(applicationContext)

        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, "measure").apply { mkdirs() }
        }

        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }


    private fun initView() {

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val navView: BottomNavigationView = binding.navView
        navView.setOnItemSelectedListener {

            if (it.itemId != navController.currentDestination?.id) {

                for (i in navController.backStack.indices) {
                    navController!!.popBackStack()
                }

                when (it.itemId) {
                    R.id.navigation_home -> navController.navigate(R.id.navigation_home)
                    R.id.loginFragment -> {
                        if (SharedPreferencesUtil.instances.getAccountId().isNullOrEmpty()) {
                            navController.navigate(R.id.loginFragment)
                        } else {
                            navController.navigate(R.id.memberFragment)
                        }
                    }
                }
            }
            return@setOnItemSelectedListener true
        }
    }

    override fun onStart() {
        super.onStart()

        checkPermission()

        getPlayStoreVersion()

        if (SharedPreferencesUtil.instances.getAccountId().isNullOrBlank()) {
            demand()
        } else {
            login()
        }

        Thread {
            findLocation()
        }.start()
    }

    /**
     * 藍牙
     */
    private fun checkBle() {

        val name = SharedPreferencesUtil.instances.getWatchName()
        val mac = SharedPreferencesUtil.instances.getWatchMac()

        if (name.isNullOrBlank() || mac.isNullOrBlank()) {
            Log.e(TAG, "尚無連線過手錶裝置")
            return
        }

        lifecycleScope.launch {

            CoroutineScope(Dispatchers.IO).launch {

                WatchUtils.instance.isBleConnect(
                    name,
                    mac,
                    success = { bleSuccess(name, mac) },
                    fail = { bleConnect(name, mac) }
                )
            }
        }
    }

    private fun bleSuccess(name: String, mac: String) {

        watchViewModel.setIsBleConnect(true)
        SharedPreferencesUtil.instances.setWatchName(name)
        SharedPreferencesUtil.instances.setWatchMac(mac)
        Log.w(TAG, "cancelAllWorker: 230")
        WatchUtils.instance.cancelAllWorker()

        watchViewModel.upDataTime.value?.let {
            if (it > DateUtil.instance.currentYmdHms()) {
                return
            }
        }

        watchViewModel.setUpDataTime(DateUtil.instance.after3MinuteYmdHms())
        Log.w(TAG, "initWatchWorker: 232")
        WatchUtils.instance.initWatchWorker()
    }

    private fun bleConnect(name: String, mac: String) {

        watchViewModel.setIsBleConnect(false)

        WatchUtils.instance.connectBle(mac) { code ->

            Log.d(TAG, "藍牙連線回傳碼: $code")
            if (code == Constants.CODE.Code_OK) bleSuccess(name, mac) else bleFail()
        }
    }

    private fun bleFail() {

        Log.w(TAG, "藍牙連線失敗")
        watchViewModel.setIsBleConnect(false)
        SharedPreferencesUtil.instances.setWatchName("")
        SharedPreferencesUtil.instances.setWatchMac("")

        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(this@MainActivity, "藍牙連線失敗", Toast.LENGTH_SHORT).show()
        }
    }


    /**
     * 權限
     */
    private fun checkPermission() {

        // 拍照錄影、定位、錄音、藍牙
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
            )
        } else {
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
            )
        }

        if (hasPermissions(*permissions)) {
            isShowCamera = false
            Log.w(TAG, "有 權限")
            checkBle()

        } else {

            Log.w(TAG, "沒 權限")
            ActivityCompat.requestPermissions(this, permissions, 200)
        }
    }

    private fun hasPermissions(vararg permissions: String): Boolean {
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 200) {

            var isGranted = true

            // 拍照錄影、定位、錄音、藍牙
            for (i in permissions.indices) {

                if (permissions[i] == Manifest.permission.ACCESS_FINE_LOCATION) {
                    continue
                }

                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    isGranted = false
                    break
                }
            }

            if (isGranted) {

                checkBle()

            } else {

                DialogUtil.instance.singleMessageDialog(
                    this,
                    "提醒",
                    "為了使用體驗，請您同意相關權限。",
                    cancelClick = {}
                )
            }
        }
    }

    /**
     * 檢查版本
     */
    private fun getPlayStoreVersion() {

        FirebaseDatabase.getInstance().getReference("Version")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val version = snapshot.value.toString()
                    Log.d(TAG, "PlayStoreVersion: $version")
                    bookViewModel.setPlayStoreVersion(version)
                }

                override fun onCancelled(error: DatabaseError) {

                    Log.w(TAG, "onCancelled: ${error.message}")
                }

            })
    }

    /**
     * 廣告ID
     */
    private fun demand() {

        lifecycleScope.launch {

            CoroutineScope(Dispatchers.IO).launch {

                val adId = try {
                    val adInfo = AdvertisingIdClient.getAdvertisingIdInfo(
                        this@MainActivity
                    )
                    adInfo.id
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }

                Log.w(TAG, "廣告ID: $adId")

                if (adId.isNullOrBlank()) {
                    return@launch
                }

                val response = apiRepository.getUidpwd(adId)

                Log.w(TAG, "login: ${response.responseMessage}")
                if (response.code == "0x0200") {
                    SharedPreferencesUtil.instances.setAccountId(response.member_id)
                    SharedPreferencesUtil.instances.setAccountPwd(response.member_pwd)
                }
            }
        }
    }

    private fun login() {

        lifecycleScope.launch {

            FirebaseApp.initializeApp(this@MainActivity)
            var token: String?
            FirebaseMessaging.getInstance().token.addOnCompleteListener(
                OnCompleteListener { task ->

                    if (!task.isSuccessful) {
                        return@OnCompleteListener
                    }

                    token = task.result
                    Log.w(TAG, "FCM_token: $token")

                    lifecycleScope.launch {

                        CoroutineScope(Dispatchers.IO).launch {

                            var adId: String?
                            try {
                                val adInfo = AdvertisingIdClient.getAdvertisingIdInfo(
                                    this@MainActivity
                                )
                                adId = adInfo.id
                            } catch (e: Exception) {
                                e.printStackTrace()
                                adId = null
                            }

                            Log.w(TAG, "廣告ID: $adId")

                            val response =
                                apiRepository.userLogin(
                                    UserLoginRequest(
                                        SharedPreferencesUtil.instances.getAccountId().toString(),
                                        SharedPreferencesUtil.instances.getAccountPwd().toString(),
                                        token,
                                        adId
                                    )
                                )

                            if (response.code != "0x0200") {
                                SharedPreferencesUtil.instances.setAccountId("")
                                SharedPreferencesUtil.instances.setAccountPwd("")
                                demand()
                            }
                        }
                    }
                }
            )
        }
    }

    /**
     * 定位、氣象
     */
    private fun findLocation() {

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            ||
            ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val client = LocationServices.getFusedLocationProviderClient(this)

            client.lastLocation.addOnCompleteListener {

                if (it.isSuccessful) {

                    // it.result must not be null
                    it.result?.let { location ->
                        searchAddress(location)
                    }
                }
            }
        }
    }

    private fun searchAddress(location: Location) {

        val geocoder = Geocoder(this, Locale.getDefault())

        try {
            Log.d(TAG, "latitude: ${location.latitude}")
            Log.d(TAG, "longitude: ${location.longitude}")
            val addresses = geocoder.getFromLocation(
                location.latitude, location.longitude, 1
            )
            if (addresses.isNotEmpty()) {
                val address = addresses[0].getAddressLine(0)
//            Toast.makeText(this, address, Toast.LENGTH_LONG).show()
                Log.d(TAG, "address(定位地址): $address")
                var adminArea = addresses[0].adminArea
                if (adminArea.contains("台")) {
                    adminArea = adminArea.replace("台", "臺")
                }
                Timber.w("adminArea: $adminArea")
                SharedPreferencesUtil.instances.setAdminArea(adminArea)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 手錶監聽
     */
    private fun initDeviceToAppCommandReceiver() {

        YCBTClient.deviceToApp { i, hashMap ->
            if (hashMap != null) {
                if (i == 0) {
                    val dataType = hashMap["dataType"] as Int
                    var data = -1

                    if (hashMap["data"] != null)
                        data = hashMap["data"] as Int

                    when (dataType) {
                        Constants.DATATYPE.DeviceTakePhoto -> {
                            when (data) {
                                0 -> { // 退出
                                    isShowCamera = false
                                    showCameraView()
                                }

                                1 -> { // 進入拍照
                                    isShowCamera = true
                                    showCameraView()
                                    openCamera()
                                }

                                2 -> { // 拍照
                                    takePicture()
                                }
                            }
                        }

                        Constants.DATATYPE.DeviceStartMusic -> {
                            when (data) {
                                0 -> { // 0. 音樂停止

                                }

                                1 -> { // 1. 播放
                                    prepareMusic()
                                }

                                2 -> { // 2. 暫停
                                    pauseSound()
                                }

                                3 -> { // 3. 上一首

                                }

                                4 -> { // 4. 下一首

                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showCameraView() {
        Thread {
            //要在Background Thread中執行的程式碼
            runOnUiThread {
                //要在Main Thread中執行的程式碼
                if (isShowCamera) {
                    binding.viewFinder.visibility = View.VISIBLE
                } else {
                    binding.viewFinder.visibility = View.GONE
                    binding.vHint.visibility = View.GONE
                }
            }
        }.start()
    }

    private fun openCamera() {

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(viewFinder.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e(TAG, "openCamera 失敗: ", e)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePicture() {

        val imageCapture = imageCapture ?: return
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(
                "yyyy-MM-dd-HH-mm-ss-SSS",
                Locale.getDefault()
            ).format(System.currentTimeMillis()) + ".jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "照片拍攝失敗: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)

                    Log.d(TAG, "拍照成功: $savedUri")

                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, savedUri)

                    val file = File(savedUri.toString())
                    val filePath = file.path
                    val lastSlashIndex = filePath.lastIndexOf("/")
                    val fileName = filePath.substring(lastSlashIndex + 1)
                    val imageOutStream: OutputStream
                    val mediaContentUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        val values = ContentValues(3)

                        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                        values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures")

                        contentResolver.run {
                            val uri = contentResolver.insert(mediaContentUri, values) ?: return
                            imageOutStream = openOutputStream(uri) ?: return
                        }
//                        this@MainActivity.contentResolver.insert(
//                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                            values
//                        )
                    } else {
//                        val file = File(
//                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath
//                                    + "Img_" + System.currentTimeMillis() + ".jpg"
//                        )
//
//                        file.parentFile?.mkdirs()

                        val imagePath = Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES
                        ).absolutePath
                        val image = File(imagePath, fileName)

                        imageOutStream = FileOutputStream(image)
                    }

                    imageOutStream.use {
                        bitmap.compress(
                            Bitmap.CompressFormat.JPEG,
                            100,
                            it
                        )
                    }

                    CoroutineScope(Dispatchers.Main).launch {

                        binding.vHint.visibility = View.VISIBLE

                        ObjectAnimator.ofFloat(
                            binding.vHint, "alpha", 1f, 0f
                        ).apply {
                            duration = 100
                            start()
                        }
                    }
                }
            }
        )
    }

    private fun prepareMusic() {
        getAudioList()
//        playSound()
    }

    private fun getAudioList() {
        val songsList: ArrayList<HashMap<String, String>> = ArrayList()
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.AudioColumns.DATA,
            MediaStore.Audio.AudioColumns.TITLE,
            MediaStore.Audio.AudioColumns.ALBUM,
            MediaStore.Audio.AudioColumns.DISPLAY_NAME
        )

        val cursor = contentResolver.query(
            uri,
            projection,
            null, //            MediaStore.Audio.Media.DISPLAY_NAME + "like ? ",
            null,
            null
        )

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                    val songName =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                    val albumName =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))
                    val albumId =
                        cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
                    val song = HashMap<String, String>()

                    song["songTitle"] = albumName + " " + songName + "___" + albumId
                    song["songPath"] = path

                    if (path.contains(".mp3"))
                        songsList.add(song)

                } while (cursor.moveToNext())
            }
        }

        Log.d(TAG, "songsList: $songsList")
    }

    private fun playSound() {
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer()
//            mMediaPlayer!!.setDataSource(
//                this,
//                Uri.parse("android.resource://"+this.packageName+"/"+R.raw.mucis))
            mMediaPlayer!!.prepare()
            mMediaPlayer!!.isLooping = true
            mMediaPlayer!!.start()
        } else {
            mMediaPlayer!!.start()

        }
    }

    private fun pauseSound() {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.stop()
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
    }

    override fun onStop() {
        super.onStop()
        if (mMediaPlayer != null) {
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}