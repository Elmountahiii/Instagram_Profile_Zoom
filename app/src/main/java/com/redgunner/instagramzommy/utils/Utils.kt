package com.redgunner.instagramzommy.utils

import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.widget.Toast



fun Context.showPermissionRequestDialog(
    title: String,
    body: String,
    callback: () -> Unit
) {
    AlertDialog.Builder(this).also {
        it.setTitle(title)
        it.setMessage(body)
        it.setPositiveButton("Ok") { _, _ ->
            callback()
        }
    }.create().show()
}
