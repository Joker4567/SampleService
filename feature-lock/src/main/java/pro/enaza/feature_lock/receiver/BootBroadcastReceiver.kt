package pro.enaza.feature_lock.receiver

import android.app.ActivityManager
import android.content.*
import android.os.Build
import pro.enaza.feature_lock.service.LockService

class BootBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        serviceStart(context, LockService::class.java)
    }

    private fun serviceStart(context: Context, serviceClass: Class<*>) {
        val isRunning = isMyServiceRunning(context, serviceClass)
        if (!isRunning) {
            val intent = Intent(context, serviceClass)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }
    }

    private fun isMyServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }
}