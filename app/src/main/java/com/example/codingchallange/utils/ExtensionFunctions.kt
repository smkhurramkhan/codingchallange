package com.example.codingchallange.utils

import android.view.View


object ExtensionFunctions {

    fun View.show() {
        this.visibility = View.VISIBLE
    }

    fun View.hide() {
        this.visibility = View.GONE
    }

    fun View.invisible() {
        this.visibility = View.INVISIBLE
    }


}