package com.jotangi.NumberHealthy.utils;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import com.jotangi.NumberHealthy.BuildConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CrashManager implements Thread.UncaughtExceptionHandler {

    private String TAG = getClass().getSimpleName() + "(TAG)";

    private static CrashManager instance;

    private Application application;

    private CrashManager(Context context) {
        application = (Application) context.getApplicationContext();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public static CrashManager getInstance(Context context) {
        CrashManager inst = instance;
        if (inst == null) {
            synchronized (CrashManager.class) {
                inst = instance;
                if (inst == null) {
                    inst = new CrashManager(context.getApplicationContext());
                    instance = inst;
                }
            }
        }
        return inst;
    }

    @Override
    public void uncaughtException(@NonNull Thread thread, @NonNull Throwable throwable) {

//        Log.d(TAG, "完全版 message: " + throwable.getCause().getMessage());
//        StringBuilder builder = new StringBuilder();
//        for (int i = 0 ; i < throwable.getCause().getStackTrace().length ; i++) {
//            builder.append("\nClassName: " + throwable.getCause().getStackTrace()[i].getClassName()
//                    + " \nMethodName: " + throwable.getCause().getStackTrace()[i].getMethodName()
//                    + " \nLineNumber: " + throwable.getCause().getStackTrace()[i].getLineNumber()
//                    + " \n====================");
//        }
//        Log.d(TAG, "清單: " + builder);


        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.TAIWAN);

        JSONObject object = new JSONObject();
        JSONObject appOB = new JSONObject();
        JSONObject phoneOB = new JSONObject();
        JSONArray listArray = new JSONArray();
        JSONObject listOB = new JSONObject();
        try {
            object.put("date", sdf.format(calendar.getTime()));

            appOB.put("code", BuildConfig.VERSION_CODE);
            appOB.put("name", BuildConfig.VERSION_NAME);
            object.put("app", appOB);

            phoneOB.put("brand", Build.BRAND);
            phoneOB.put("model", Build.MODEL);
            phoneOB.put("android", Build.VERSION.RELEASE);
            object.put("phone", phoneOB);

            if (throwable.getCause().getMessage() != null) {
                object.put("message", throwable.getCause().getMessage());
            }


            for (int i = 0; i < throwable.getCause().getStackTrace().length; i++) {
                listOB.put("class", throwable.getCause().getStackTrace()[i].getClassName());
                listOB.put("method", throwable.getCause().getStackTrace()[i].getMethodName());
                listOB.put("line", throwable.getCause().getStackTrace()[i].getLineNumber());
                listArray.put(listOB);
                listOB = new JSONObject();
            }
            object.put("list", listArray);
            Log.e("TAG", "ob: \n" + object);

            new Thread(() -> {

                String urlPath = "http://211.20.181.118:9080/androidreport/report.php";
                try {
                    URL url = new URL(urlPath);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    con.setRequestProperty("Accept", "application/json");

                    // 抓取輸出管道
                    OutputStream outputStream = con.getOutputStream();
                    // 建立輸出物件
                    OutputStreamWriter osw
                            = new OutputStreamWriter(outputStream);
                    // 把連線樣式輸出
                    osw.write(object.toString());

                    // 文字容量超過8KB就要使用，為了安全起見一律使用
                    osw.flush();
                    // 關閉輸出物件
                    osw.close();

                    // 抓取輸入管道
                    InputStream inputStream = con.getInputStream();
                    // 建立輸入物件
                    InputStreamReader isr = new InputStreamReader(inputStream);
                    // 建立容器
                    BufferedReader reader = new BufferedReader(isr);

                    StringBuilder sb = new StringBuilder();
                    // 文字內容
                    String line = null;
                    // 讀取一行文字，直到null
                    while ((line = reader.readLine()) != null) {
                        // sb如有內容，則在之後添加
                        sb.append(line + "\n");
                    }

                    // 把sb轉文字
                    String response = sb.toString();

                    Log.d(TAG, "伺服器回應：" + response);

                    reader.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }).start();


        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 第一種：先存起來下次開啟 App 啟動寄信功能，但如果 application 有問題就會存擋失敗。
//        SharedPreferences sp = application.getSharedPreferences("Error", Context.MODE_PRIVATE);
//        sp.edit().putBoolean("isError", true).apply();
//        sp.edit().putString("message",str).apply();


        // 第二種：直接送後台(FireBase)，因為不會用到 application 相對成功率高。
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference reference = database.getReference(sdf.format(calendar.getTime()));
//        reference.setValue(object.toString());
    }
}
