package com.example.util

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object HafeziPageDownloader {

    private const val USER_AGENT = "Mozilla/5.0 (Android 13; Mobile)"

    private val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
        override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
        override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
    })

    private val sslSocketFactory by lazy {
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, SecureRandom())
        sslContext.socketFactory
    }

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier(HostnameVerifier { _, _ -> true })
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .followRedirects(true)
            .followSslRedirects(true)
            .build()
    }

    fun getPageFile(context: Context, pageNumber: Int): File {
        return File(context.filesDir, "hafezi_page_${pageNumber}.png")
    }

    fun deletePageFile(context: Context, pageNumber: Int) {
        try {
            val file = getPageFile(context, pageNumber)
            if (file.exists()) file.delete()
            val tmp = File(context.filesDir, "hafezi_page_${pageNumber}.tmp")
            if (tmp.exists()) tmp.delete()
        } catch (_: Exception) {
        }
    }

    suspend fun downloadPage(context: Context, pageNumber: Int): Result<File> = withContext(Dispatchers.IO) {
        val destFile = getPageFile(context, pageNumber)

        // Step 2 optimization: If file already exists and is non-empty, skip download
        if (destFile.exists() && destFile.length() > 1000) {
            return@withContext Result.success(destFile)
        }

        val paddedPage = String.format(Locale.US, "%03d", pageNumber.coerceIn(1, 604))
        val candidateUrls = listOf(
            // Primary URL: EveryAyah CDN
            "https://everyayah.com/data/quranpng/${pageNumber}.png",
            // Fallback URL 1: GitHub Raw images (3-digit zero padded)
            "https://raw.githubusercontent.com/quran/quran.com-images/master/images_1024/page$paddedPage.png",
            // Fallback URL 2: Android Quran CDN
            "https://android.quran.com/data/images_1024/page_$paddedPage.png"
        )

        val tmpFile = File(context.filesDir, "hafezi_page_${pageNumber}.tmp")
        var lastException: Exception? = null

        for (url in candidateUrls) {
            try {
                val request = Request.Builder()
                    .url(url)
                    .header("User-Agent", USER_AGENT)
                    .header("Accept", "image/*,*/*")
                    .build()

                val response = client.newCall(request).execute()
                response.use { res ->
                    if (res.isSuccessful) {
                        val body = res.body ?: return@use
                        FileOutputStream(tmpFile).use { output ->
                            body.byteStream().copyTo(output)
                        }
                        if (tmpFile.exists() && tmpFile.length() > 500) {
                            if (destFile.exists()) destFile.delete()
                            if (tmpFile.renameTo(destFile)) {
                                return@withContext Result.success(destFile)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                lastException = e
            } finally {
                if (tmpFile.exists()) {
                    tmpFile.delete()
                }
            }
        }

        deletePageFile(context, pageNumber)
        Result.failure(lastException ?: RuntimeException("Failed to download Page $pageNumber from all mirrors"))
    }
}
