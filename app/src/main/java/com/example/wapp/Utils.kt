package com.example.wapp

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.content.res.AppCompatResources
import com.example.wapp.data.remote.api.MapboxRetrofitClient
import com.example.wapp.screen.maps.MapsViewModel
import com.mapbox.api.geocoding.v5.GeocodingCriteria
import com.mapbox.api.geocoding.v5.MapboxGeocoding
import com.mapbox.geojson.Point
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone


fun bitmapFromDrawableRes(context: Context, resourceId: Int): Bitmap? {
    val drawable = AppCompatResources.getDrawable(context, resourceId)
    return convertDrawableToBitmap(drawable)
}

fun convertDrawableToBitmap(sourceDrawable: Drawable?): Bitmap? {
    if (sourceDrawable == null) {
        return null
    }
    return if (sourceDrawable is BitmapDrawable) {
        sourceDrawable.bitmap
    } else {
        val constantState = sourceDrawable.constantState ?: return null
        val drawable = constantState.newDrawable().mutate()
        val bitmap: Bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth, drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        bitmap
    }
}

fun saveUriToFile(context: Context, uri: Uri): File {
    val bitmap = uriToBitmap(context, uri)
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val resizedBitmap = resizeBitmap(bitmap!!)
    val file = File(context.cacheDir, "JPEG_${timeStamp}.jpeg")
    file.outputStream().use {
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
    }
    return file
}
fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
    return try {
        val contentResolver = context.contentResolver
        contentResolver.openInputStream(uri)?.use { inputStream ->
            BitmapFactory.decodeStream(inputStream)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}


fun saveBitmapToFile(context: Context, bitmap: Bitmap): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val resizedBitmap = resizeBitmap(bitmap)

    val file = File(context.cacheDir, "JPEG_${timeStamp}.jpeg")
    file.outputStream().use {
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
    }
    return file
}


fun getCurrentTimestamp(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone("UTC")
    return dateFormat.format(Date())
}
fun resizeBitmap(bitmap: Bitmap, newWidth: Int=1950, newHeight: Int=1080): Bitmap {

    val resizedBitmap = Bitmap.createBitmap(newWidth, newHeight, bitmap.config)

    val canvas = Canvas(resizedBitmap)
    val paint = Paint()
    val rect = Rect(0, 0, newWidth, newHeight)
    canvas.drawBitmap(bitmap, null, rect, paint)

    return resizedBitmap
}


fun hideKeyboard(context: Context) {
    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val currentFocus = (context as Activity).currentFocus
    currentFocus?.let {
        inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
    }
}




fun getStreetName(location: Point, accessToken: String, mapsViewModel: MapsViewModel) {
    val geocoding = MapboxGeocoding.builder()
        .accessToken(accessToken)
        .query(location)
        .geocodingTypes(
            GeocodingCriteria.TYPE_ADDRESS,
            GeocodingCriteria.TYPE_POI
        )
        .build()

    geocoding.enqueueCall(object : retrofit2.Callback<com.mapbox.api.geocoding.v5.models.GeocodingResponse> {
        override fun onResponse(
            call: retrofit2.Call<com.mapbox.api.geocoding.v5.models.GeocodingResponse>,
            response: retrofit2.Response<com.mapbox.api.geocoding.v5.models.GeocodingResponse>
        ) {
            if (response.isSuccessful) {
                val results = response.body()?.features()
                Log.d("Mapbox", "Full response: ${response.body()}")
                Log.e("Mapbox", "Error response: ${response.errorBody()?.string()}")

                if (!results.isNullOrEmpty()) {
                    val streetName = results[0].placeName()
                    Log.d("Mapbox", "Nama Jalan: $streetName")
                    mapsViewModel.updateStreetName(streetName)
                } else {
                    Log.d("Mapbox", "Tidak ada hasil geocoding untuk lokasi ini.")
                    mapsViewModel.updateStreetName("Tidak ada hasil geocoding untuk lokasi ini.")

                }
            } else {
                mapsViewModel.updateStreetName("Tidak ada hasil geocoding untuk lokasi ini.")
            }
        }

        override fun onFailure(
            call: retrofit2.Call<com.mapbox.api.geocoding.v5.models.GeocodingResponse>,
            t: Throwable
        ) {
            Log.e("Mapbox", "Error: ${t.localizedMessage}")
        }
    })
}

