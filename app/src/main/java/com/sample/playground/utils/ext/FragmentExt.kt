package com.sample.playground.utils.ext

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment

inline fun <reified T : Any> Fragment.launchActivityResult(
    requestCode: Int = -1,
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {

    val intent = newIntent<T>(this.activity!!)
    intent.init()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        startActivityForResult(intent, requestCode, options)
    } else {
        startActivityForResult(intent, requestCode)
    }
}

inline fun <reified T : Any> Fragment.launchActivity(
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}) {

    val intent = newIntent<T>(this.activity!!)
    intent.init()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        startActivity(intent, options)
    } else {
        startActivity(intent)
    }
}