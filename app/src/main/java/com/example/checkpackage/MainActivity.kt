package com.example.checkpackage

import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private val tagLog : String = javaClass.simpleName


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        thread {
            // call the method from a background thread
            checkChangedPackages(applicationContext)
        }

    }

    private fun checkChangedPackages(context: Context) {

        val packageManagerApps = context.packageManager
        val sequenceNumber = getSequenceNumber(context)
        Log.d(tagLog, "sequenceNumber = $sequenceNumber")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val changedPackages = packageManagerApps.getChangedPackages(sequenceNumber)

            if (changedPackages != null) {
                // Packages are changed

                // Get the list of changed packages
                // the list includes new, updated and deleted apps
                val changedPackagesNames = changedPackages.packageNames

                var appName: CharSequence

                for (packageName in changedPackagesNames) {
                    try {
                        appName = packageManagerApps.getApplicationLabel(
                            packageManagerApps.getApplicationInfo(
                                packageName, 0,
                            )
                        )

                        // Either a new or an updated app
                        Log.d(
                            tagLog,
                            "New Or Updated App: $packageName , appName = ${appName.toString()}"
                        )
                    } catch (e: PackageManager.NameNotFoundException) {
                        // The app is deleted
                        Log.d(tagLog, "Deleted App: $packageName")
                    }
                }
                saveSequenceNumber(context, changedPackages.sequenceNumber)
            } else {
                // packages not changed
            }
        }
    }

    private fun getSequenceNumber(context: Context): Int {
        val sharedPrefFile = context.getSharedPreferences("SPF", MODE_PRIVATE)
        return sharedPrefFile.getInt("sequence_number", 0)
    }

    private fun saveSequenceNumber(context: Context, newSequenceNumber: Int) {
        val sharedPrefFile = context.getSharedPreferences("SPF", MODE_PRIVATE)
        val editor = sharedPrefFile.edit()
        editor.putInt("sequence_number", newSequenceNumber)
        editor.apply()
    }
}