package com.freeplaytv.shortcut

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast

private const val LAUNCHERX_PACKAGE = "com.google.android.apps.tv.launcherx"
private const val FREEPLAY_DEFAULT_URL = "https://tv.google.com/freeplay/default"

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(FREEPLAY_DEFAULT_URL)).apply {
            setPackage(LAUNCHERX_PACKAGE)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                this,
                "Google TV Home (launcherx) isn't available on this device",
                Toast.LENGTH_LONG
            ).show()
        }

        finish()
    }
}
