package com.example.aacinaction.workmanager

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.work.*
import java.util.*
import java.util.concurrent.TimeUnit

class UploadWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    //创建后台任务
    override fun doWork(): Result {
//        Log.e("wcc", "${Thread.currentThread()}")
//        Thread.sleep(2000) //模拟
//        return Result.success()
//        return Result.retry()

        val imageUri = inputData.getString(Task.KEY_IMAGE_URI) //取出输入数据
        Log.e("wcc", imageUri)
        val repsonseUri = uploadImage(imageUri) //上传完成后返回输出数据
        return Result.success(workDataOf(Task.KEY_IMAGE_URI to repsonseUri)) //异步任务完成后，设置返回数据
    }
    /*
        Worker执行状态会通知WorkerManager，有如下3种状态：
        Result.success()：已成功完成
        Result.failure()：已失败
        Result.retry()：需要稍后重试
     */

    private fun uploadImage(uri: String?) : String{
        //Do the work here, update image

        return "https://...."
    }

}


class Task {
    fun start(context: Context) {
        val constraints = Constraints.Builder()
//            .setRequiresDeviceIdle(true) //设备空闲
//            .setRequiresCharging(true) //电源充电时
            .build()

        //作为启动任务的输入数据
        val data = workDataOf(KEY_IMAGE_URI to "https://www.baidu.com/abc/1.jpeg")

        val uploadWorkerRequest = OneTimeWorkRequestBuilder<UploadWorker>()
            .setConstraints(constraints)
            .setInitialDelay(10, TimeUnit.SECONDS) //任务加入队列后，再等待10秒才运行任务；
//            .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.SECONDS)
            .setInputData(data)
            .addTag("cleanup")
            .build()

        //使用WorkManager调度WorkRequest
        WorkManager.getInstance(context).enqueue(uploadWorkerRequest)

        //取消tag标记的所有任务
        WorkManager.getInstance(context).cancelAllWorkByTag("cleanup")
        //返回有该tag标记的LiveData
        val livedata: LiveData<MutableList<WorkInfo>> = WorkManager.getInstance(context).getWorkInfosByTagLiveData("cleanup")

        //观察工作状态
//        WorkManager.getInstance(context).getWorkInfoByIdLiveData(UUID.randomUUID())
//            .observe(lifecycleOwner, androidx.lifecycle.Observer {workInfo ->
//                if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {
//                    display message
//                }
//            })


    }

    /*
        若设置多个约束，则需要满足所有约束才会运行；
        若任务运行过程中，某个约束不满足了，WorkManager将停止运行任务；
            等到约束重新满足时，系统会尝试重新运行任务；

        若Worker返回Result.retry()，配合WorkerRequest调用setBackoffCriteria方法设置延迟策略，可以重新执行任务；

        Data类用于在线程间传递数据，其大小限制为10KB。若数据量较大，可以用Room实现；

        任务的工作状态：
            BLOCKED：有尚未完成的前提工作
            ENQUEUED：工作满足约束和时机条件后立即运行
            RUNNING：工作执行中
            SUCCEEDED：若Worker返回Result.success()，一种终止状态；
            FAILED：若Worker返回Result.failure()，一种终止状态；
            CANCELLED：取消尚未终止的WorkRequest时，所有工作不会再运行；
     */

    companion object {
        val KEY_IMAGE_URI = "key_image_uri"
    }


}

