package com.sample.playground.utils.ext

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadFromUrl(url: String, context: Context) {
    Glide.with(context).load(url).into(this)
}