package pro.enaza.core_ui.activity

import android.app.ActivityManager
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import pro.enaza.core_ui.R
import pro.enaza.feature_lock.receiver.BootBroadcastReceiver
import pro.enaza.feature_lock.service.LockService


@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.main_activity) {
    override fun onStart() {
        super.onStart()
        startService(Intent(this, LockService::class.java))
        registerReceiverAlarm()
        registerAlarm()
    }

    override fun onResume() {
        super.onResume()
        val isBackground = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) isBackground() else true
        if(isBackground.not()) {
            startActivity(Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS, Uri.parse("package:$packageName")))
        }
    }

    private fun registerAlarm() {
        val receiver = ComponentName(this, BootBroadcastReceiver::class.java)

        packageManager.setComponentEnabledSetting(
                receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
        )
    }

    private fun registerReceiverAlarm() {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        val alarmIntent = Intent(this, BootBroadcastReceiver::class.java)
                .apply {
                    addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
                    addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
                }
        var pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0)

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, 0, pendingIntent)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, 0, pendingIntent)
            }
            else -> {
                alarmManager.set(AlarmManager.RTC_WAKEUP, 0, pendingIntent)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun isBackground() : Boolean {
        return (getSystemService(ACTIVITY_SERVICE) as ActivityManager).isBackgroundRestricted
    }

    /*
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                val pkg = packageName
                val pm = getSystemService(PowerManager::class.java)
                if (!pm.isIgnoringBatteryOptimizations(pkg)) {
                    val i = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                            .setData(Uri.parse("package:$pkg"))
                    startActivity(i)
                }
            }
    */
}