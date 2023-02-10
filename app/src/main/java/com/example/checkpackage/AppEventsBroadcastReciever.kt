package com.example.checkpackage

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast

class AppEventsBroadcastReceiver : BroadcastReceiver() {

    private val tagLog : String = javaClass.simpleName

    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent != null && context != null) {

            // Check this condition because the broadcast receiver
            // is getting triggered on some devices running above Oreo
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {

                val packageManager = context.packageManager
                val appUid = intent.getIntExtra(Intent.EXTRA_PACKAGE_NAME, 0)

                if (intent.action == "android.intent.action.PACKAGE_FULLY_REMOVED") {
                    Log.d(tagLog, "PACKAGE_FULLY_REMOVED $appUid")
                    Toast.makeText(context,"REMOVED $appUid", Toast.LENGTH_SHORT).show()
                } else {
                    val applicationInfo = packageManager?.getApplicationInfo(
                        packageManager.getNameForUid(appUid)!!, PackageManager.GET_META_DATA
                    )!!

                    val appName = packageManager.getApplicationLabel(applicationInfo).toString()
                    val appPackageName = applicationInfo.packageName

                    if (intent.action == "android.intent.action.PACKAGE_ADDED") {
                        Log.d(tagLog, "ADDED $appPackageName , $appName")
                        Toast.makeText(context,"ADDED $appPackageName , $appName", Toast.LENGTH_SHORT).show()
                    } else if (intent.action == "android.intent.action.PACKAGE_REPLACED") {
                        Log.d(tagLog, "PACKAGE_REPLACED $appPackageName , $appName")
                        Toast.makeText(context,"REPLACED $appPackageName , $appName", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}