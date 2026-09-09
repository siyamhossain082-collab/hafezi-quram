package com.example.util

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import okhttp3.OkHttpClient
import java.io.File
import java.util.concurrent.TimeUnit

object QuranImageLoaderProvider {
    private var instance: ImageLoader? = null

    fun get(context: Context): ImageLoader {
        return instance ?: synchronized(this) {
            instance ?: buildLoader(context.applicationContext).also { instance = it }
        }
    }

    private fun buildLoader(context: Context): ImageLoader {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        val cacheDir = File(context.cacheDir, "hafezi_quran_cache")

        return ImageLoader.Builder(context)
            .okHttpClient(okHttpClient)
            .memoryCache {
                MemoryCache.Builder(context)
                    .maxSizePercent(0.25)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir)
                    .maxSizeBytes(500L * 1024 * 1024) // 500 MB disk cache for full offline Quran
                    .build()
            }
            .respectCacheHeaders(false) // Enables offline rendering from disk cache indefinitely
            .build()
    }
}

@Composable
fun rememberQuranImageLoader(): ImageLoader {
    val context = LocalContext.current
    return remember(context) {
        QuranImageLoaderProvider.get(context)
    }
}
