package com.flood_android.util

import android.app.Activity
import android.content.Context
import android.os.SystemClock
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.toast(msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Activity.myNameIS09(){

}