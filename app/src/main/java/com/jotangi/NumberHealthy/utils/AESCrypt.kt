package com.jotangi.NumberHealthy.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.nio.charset.StandardCharsets
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object AESCrypt {
    // 記得定義一下你的 key
    private const val key: String = "YcL+NyCRl5FYMWhozdV5V8eu6qv3cLDL"

    // 這裡是宣告加解密的方法
    private const val AES = "AES"
    private val KEY_VI = "53758995@jotangi".toByteArray() //"c558Gq0YQK2QUlMc".toByteArray()
    private const val CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding"

    @RequiresApi(Build.VERSION_CODES.O)
    private val base64Encoder = Base64.getEncoder()

    @RequiresApi(Build.VERSION_CODES.O)
    private val base64Decoder = Base64.getDecoder()

    // 加密使用的方法
    @RequiresApi(Build.VERSION_CODES.O)
    fun encrypt(input: String): String {
        try {
            val secretKey: SecretKey = SecretKeySpec(key.toByteArray(), AES)
            val cipher = Cipher.getInstance(CIPHER_ALGORITHM)
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, IvParameterSpec(KEY_VI))

            // 獲取加密內容的位元組陣列(這裡要設定為utf-8)不然內容中如果有中文和英文混合中文就會解密為亂碼
            val byteEncode: ByteArray = input.toByteArray(StandardCharsets.UTF_8)

            // 根據密碼器的初始化方式加密
            val byteAES = cipher.doFinal(byteEncode)

            // 將加密後的資料轉換為字串
            return base64Encoder.encodeToString(byteAES)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return input
    }

    // 解密使用的方法
    fun decrypt(input: String): String {
//        val cipher = Cipher.getInstance(transformation)
//        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec)
//        val encrypt = cipher.doFinal(input.hexAsByteArray)
//        return String(encrypt)
        return input
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun String.aesEncrypt(): String = encrypt(this)

    fun String.aesDecrypt(): String = decrypt(this)
}