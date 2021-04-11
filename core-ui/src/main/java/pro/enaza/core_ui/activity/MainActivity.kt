package pro.enaza.core_ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import pro.enaza.core_ui.R
import pro.enaza.feature_lock.service.LockService

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.main_activity) {
    override fun onStart() {
        super.onStart()
        startService(Intent(this, LockService::class.java))
    }
}