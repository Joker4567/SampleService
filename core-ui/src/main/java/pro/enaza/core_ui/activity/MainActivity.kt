package pro.enaza.core_ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.main_activity.*
import pro.enaza.core_ui.R
import pro.enaza.feature_lock.work.SyncWorker
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.main_activity) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initWorker()
    }

    override fun onStart() {
        super.onStart()
        startWorkSync()

    }

    private fun initWorker() {
        WorkManager.getInstance(this)
                .getWorkInfosForUniqueWorkLiveData(WORK_ID)
                .observe(this, { if (it.isNotEmpty()) handleWorkInfo(it.first()) })
    }

    private fun handleWorkInfo(workInfo: WorkInfo) {
        val isFinished = workInfo.state.isFinished
        if (isFinished) {
            tvStatus.text = "Worker is started"
        }
    }

    private fun startWorkSync() {

        val workData = workDataOf(
                SyncWorker.KEY_RADIUS to "12"
        )

        val workConstraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_ROAMING)
                .build()

        val workRequest = PeriodicWorkRequestBuilder<SyncWorker>(15, TimeUnit.MINUTES)
                .setInputData(workData)
                .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.SECONDS)
                .setConstraints(workConstraints)
                .build()

        WorkManager.getInstance(this).enqueue(workRequest)
    }

    private fun stopWorker() {
        WorkManager.getInstance(applicationContext).cancelUniqueWork(WORK_ID)
    }

    companion object {
        private const val WORK_ID = "download_work"
    }
}