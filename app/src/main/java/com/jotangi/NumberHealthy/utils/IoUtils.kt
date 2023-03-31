package com.jotangi.NumberHealthy.utils

import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

class IoUtils {

    companion object {

        private val TAG: String = "${javaClass.simpleName}(TAG)"

        fun pictureUrlToFile(pictureUrl: String, filePath: String): Boolean {

            var byteArray = ByteArray(0)

            try {

                val url = URL(pictureUrl)
                val httpURLConnection = url.openConnection() as HttpURLConnection
                httpURLConnection.doInput = true
                httpURLConnection.connect()

                val responseCode = httpURLConnection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = httpURLConnection.inputStream
                    val baos = ByteArrayOutputStream()
                    val buffer = ByteArray(1024)
                    var len = 0
                    while (len != -1) {
                        baos.write(buffer, 0, len)
                        len = inputStream.read(buffer)
                    }

                    byteArray = baos.toByteArray()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

            if (byteArray.isEmpty()) {
                Log.e(TAG, "URL 沒有資料: ")
                return false
            }

            try {
                val fileOutputStream = FileOutputStream(filePath)
                fileOutputStream.write(byteArray)
                fileOutputStream.flush()
                fileOutputStream.close()
                return true
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return false
        }
    }
}