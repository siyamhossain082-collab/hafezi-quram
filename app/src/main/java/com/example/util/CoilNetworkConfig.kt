package com.example.util

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.io.File
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object CoilNetworkConfig {

    private const val USER_AGENT = "Mozilla/5.0 (Android 13; Mobile)"
    private const val DISK_CACHE_DIR_NAME = "hafezi_quran_cache"
    private const val DISK_CACHE_SIZE = 500L * 1024 * 1024 // 500 MB

    private var sharedOkHttpClient: OkHttpClient? = null
    private var sharedImageLoader: ImageLoader? = null

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

    fun getQuranPageUrl(pageNumber: Int): String {
        val paddedPage = String.format(Locale.US, "%03d", pageNumber.coerceIn(1, 604))
        return "https://raw.githubusercontent.com/quran/quran.com-images/master/images_1024/page$paddedPage.png"
    }

    fun getFallbackQuranPageUrl(pageNumber: Int): String {
        val paddedPage = String.format(Locale.US, "%03d", pageNumber.coerceIn(1, 604))
        return "https://android.quran.com/data/images_1024/page$paddedPage.png"
    }

    fun getCdnUrls(pageNumber: Int): List<String> {
        val clamped = pageNumber.coerceIn(1, 604)
        val formatted3 = String.format(Locale.US, "%03d", clamped)
        return listOf(
            // Primary CDN: GitHub raw images with 3-digit zero padding
            "https://raw.githubusercontent.com/quran/quran.com-images/master/images_1024/page$formatted3.png",
            // Fallback CDN 1: Android Quran CDN (without underscore)
            "https://android.quran.com/data/images_1024/page$formatted3.png",
            // Fallback CDN 2: Android Quran CDN (with underscore)
            "https://android.quran.com/data/images_1024/page_$formatted3.png",
            // Fallback CDN 3: EveryAyah CDN
            "https://everyayah.com/data/quranpng/$clamped.png"
        )
    }

    fun clearCacheForPage(context: Context, pageNumber: Int) {
        val loader = getImageLoader(context)
        val urls = getCdnUrls(pageNumber)
        urls.forEach { url ->
            try {
                loader.diskCache?.remove(url)
                loader.memoryCache?.remove(MemoryCache.Key(url))
            } catch (_: Exception) {
            }
        }
    }

    fun getOkHttpClient(): OkHttpClient {
        return sharedOkHttpClient ?: synchronized(this) {
            sharedOkHttpClient ?: OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                .hostnameVerifier(HostnameVerifier { _, _ -> true })
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(25, TimeUnit.SECONDS)
                .followRedirects(true)
                .followSslRedirects(true)
                .addInterceptor(Interceptor { chain ->
                    val request = chain.request().newBuilder()
                        .header("User-Agent", USER_AGENT)
                        .header("Accept", "image/avif,image/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8")
                        .header("Accept-Language", "en-US,en;q=0.9")
                        .header("Connection", "keep-alive")
                        .build()
                    chain.proceed(request)
                })
                .build().also { sharedOkHttpClient = it }
        }
    }

    fun getImageLoader(context: Context): ImageLoader {
        return sharedImageLoader ?: synchronized(this) {
            sharedImageLoader ?: buildImageLoader(context.applicationContext).also {
                sharedImageLoader = it
            }
        }
    }

    private fun buildImageLoader(context: Context): ImageLoader {
        val cacheDir = File(context.cacheDir, DISK_CACHE_DIR_NAME)

        return ImageLoader.Builder(context)
            .okHttpClient(getOkHttpClient())
            .memoryCache {
                MemoryCache.Builder(context)
                    .maxSizePercent(0.25)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir)
                    .maxSizeBytes(DISK_CACHE_SIZE)
                    .build()
            }
            .respectCacheHeaders(false) // Enables offline usage indefinitely without cache expiration blocking
            .build()
    }

    fun clearCacheForUrl(context: Context, url: String) {
        try {
            val loader = getImageLoader(context)
            loader.diskCache?.remove(url)
            loader.memoryCache?.remove(MemoryCache.Key(url))
        } catch (_: Exception) {
        }
    }
}

@Composable
fun rememberCoilImageLoader(): ImageLoader {
    val context = LocalContext.current
    return remember(context) {
        CoilNetworkConfig.getImageLoader(context)
    }
}
