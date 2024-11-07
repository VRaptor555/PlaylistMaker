package com.example.playlistmaker.utils

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.TypedValue
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Locale

fun dpToPx(dp: Float, context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics
    ).toInt()
}

fun getURLImage500(urlImage: String) = urlImage.replaceAfterLast('/', "512x512bb.jpg")

fun <T : Serializable?> getSerializable(activity: Activity, name: String, clazz: Class<T>): T
{
    return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        activity.intent.getSerializableExtra(name, clazz)!!
    else
        activity.intent.getSerializableExtra(name) as T
}

fun timeMillisToMin(timeMillis: String): String {
    val trackTime: Long = java.lang.Long.parseLong(timeMillis)
    return SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTime)
}
