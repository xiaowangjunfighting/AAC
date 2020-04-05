package com.example.aacinaction.workmanager

import android.content.Context
import androidx.work.*
import kotlinx.coroutines.delay
import java.util.*
import java.util.concurrent.TimeUnit

class ProgressWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    companion object {
        const val PROGRESS = "progress"

        const val FLAG = "ProgressWorker"
    }

    //异步任务中更新进度
    override suspend fun doWork(): Result {
        setProgress(workDataOf(PROGRESS to 0)) //刚开始进度为0
        delay(1000) //模拟异步任务
        setProgress(workDataOf(PROGRESS to 100)) //完成后进度为100
        return Result.success()
    }
}

class Task1 {

    //主线程中观察进度，并更新UI
    fun start(context: Context) {

/*
        WorkManager.getInstance(context)
            .getWorkInfoByIdLiveData(UUID.randomUUID())
            .observe(lifecycleOwner, androidx.lifecycle.Observer {workInfo ->
                if (workInfo != null) {
                    val progress = workInfo.progress
                    val value = progress.getInt(ProgressWorker.PROGRESS, 0)
                    //update UI
                }
            })
*/

        val workRequest1 = OneTimeWorkRequestBuilder<ProgressWorker>().build()
        val workRequest2 = OneTimeWorkRequestBuilder<ProgressWorker>().build()

        val compress = OneTimeWorkRequestBuilder<ProgressWorker>()
            .setInputMerger(ArrayCreatingInputMerger::class) //合并多个输入workRequest1，workRequest2
            .build()

        val workRequest4 = OneTimeWorkRequestBuilder<ProgressWorker>()
            .build()

        //WorkManager创建工作链并排队
        WorkManager.getInstance(context)
            .beginWith(listOf(workRequest1, workRequest2)) //父级
            .then(compress) //子级
            .then(workRequest4)
            .enqueue()
        /*
            父级OneTimeWorkRequest的输出作为输入传递给子级；

            父级完成Result.success()，子级才可能进入ENQUEUED状态；
            父级失败Result.failure()，子级也会标记为FAILED;
            父级取消，子级也会标记为CANCELLED;
         */

        //取消工作
        WorkManager.getInstance(context).cancelAllWork()
        WorkManager.getInstance(context).cancelAllWorkByTag("tag")
        WorkManager.getInstance(context).cancelUniqueWork("unique")
        WorkManager.getInstance(context).cancelWorkById(UUID.randomUUID())


        //间隔一小时，周期性执行任务
        val constrait = Constraints.Builder()
            .setRequiresCharging(true) //充电状态
            .build()
        val pwr = PeriodicWorkRequestBuilder<ProgressWorker>(1, TimeUnit.HOURS)
            .setConstraints(constrait)
            .build()
        WorkManager.getInstance(context).enqueue(pwr)


        //唯一工作
        val wr = OneTimeWorkRequestBuilder<ProgressWorker>()
            .build()
        WorkManager.getInstance(context)
            .enqueueUniqueWork(ProgressWorker.FLAG, ExistingWorkPolicy.REPLACE, wr)
        /*
            调用WorkManager$enqueueUniqueWork方法可以给一个工作序列添加唯一的flag；
                若之前已存在一个这样flag的工作序列，WorkManager给出了3种处理方式；

            策略的值可能是：
                ExistingWorkPolicy.REPLACE：取消现有的工作序列，直接用现在的替换；
                ExistingWorkPolicy.KEEP: 保存现有的工作序列，取消新的工作序列；
                ExistingWorkPolicy.APPEND: 现有序列的最后一个任务完成后，运行新序列的第一个任务；
                    注意：APPEND不能和PeriodicWorkRequest配合使用，因为其任务会周期性循环，没有具体的完成时机；
         */

        //工作链
        val wr1 = OneTimeWorkRequestBuilder<ProgressWorker>()
            .build()
        val wr2 = OneTimeWorkRequestBuilder<ProgressWorker>()
            .build()

        WorkManager.getInstance(context)
            .beginUniqueWork(ProgressWorker.FLAG, ExistingWorkPolicy.APPEND, wr1)
            .then(wr2)
            .enqueue()

    }
}