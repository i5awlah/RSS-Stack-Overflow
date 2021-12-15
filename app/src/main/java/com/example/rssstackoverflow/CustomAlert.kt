package com.example.rssstackoverflow

import android.content.Context
import androidx.appcompat.app.AlertDialog

class CustomAlert(private val context: Context, title: String, summary: String) {
    init {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setMessage(summary)

            .setPositiveButton("OK")
            {
                    dialog, _ -> dialog.cancel()
            }

        val alert = dialogBuilder.create()
        alert.setTitle(title)
        alert.show()
    }
}