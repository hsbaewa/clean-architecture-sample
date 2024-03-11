package kr.co.hs.cleanarchitecturesample.extension

import android.content.Context
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.request.Disposable
import coil.request.ImageRequest
import kr.co.hs.cleanarchitecturesample.R
import kr.co.hs.cleanarchitecturesample.extension.NumberExt.dp
import java.lang.ref.Reference
import java.lang.ref.WeakReference
import java.net.URL

object CoilExt {
    private val Context.imageLoader: ImageLoader
        get() = ImageLoader.Builder(this)
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    .maxSizeBytes(1024 * 1024 * 50)
                    .build()
            }
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.25)
                    .build()
            }
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()
    private var imageLoaderReference: Reference<ImageLoader>? = null

    fun ImageView.loadURL(
        data: URL?,
        useProgressIndicator: Boolean = false
    ): Disposable {
        val loading = if (useProgressIndicator) {
            CircularProgressDrawable(context).apply {
                strokeWidth = 2.dp
                centerRadius = 10.dp
                setColorSchemeColors(ContextCompat.getColor(context, R.color.purple_500))
                start()
            }
        } else null

        val imageLoader = imageLoaderReference
            ?.get()
            ?: context.imageLoader.also { imageLoaderReference = WeakReference(it) }

        return imageLoader.enqueue(
            ImageRequest.Builder(context)
                .data(data.toString())
                .placeholder(loading)
                .target(this)
                .crossfade(true)
                .build()
        )
    }
}