package ru.vraptor.playlistmaker.utils

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.TypedValue
import androidx.core.bundle.Bundle
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Locale

fun pxToDp(dp: Float, context: Context): Int {
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

fun <T: Serializable?> getSerializable(bundle: Bundle, name: String, clazz: Class<T>): T {
    return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        bundle.getSerializable(name, clazz)!!
    else
        bundle.getSerializable(name) as T
}

fun timeMillisToMin(timeMillis: String): String {
    val trackTime: Long = java.lang.Long.parseLong(timeMillis)
    return timeMillisToMin(trackTime)
}

fun timeMillisToMin(timeMillis: Long): String {
    return SimpleDateFormat("mm:ss", Locale.getDefault()).format(timeMillis)
}

fun timeMillisToMin(timeMillis: Int): String {
    return SimpleDateFormat("mm:ss", Locale.getDefault()).format(timeMillis)
}
