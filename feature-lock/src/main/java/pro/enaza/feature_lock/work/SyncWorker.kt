package pro.enaza.feature_lock.work

import android.content.*
import androidx.hilt.work.HiltWorker
import androidx.work.*
import dagger.assisted.*
import pro.enaza.feature_lock.service.LockService

@HiltWorker
class SyncWorker @AssistedInject constructor(
        @Assisted appContext: Context,
        @Assisted params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        this.applicationContext.startService(Intent(this.applicationContext, LockService::class.java))
        return Result.success()
    }

    companion object {
        const val KEY_RADIUS = "download url"
    }
}