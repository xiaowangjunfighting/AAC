package com.example.aacinaction.workmanager

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

class CoroutineDownloadWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    //设置coroutineContext方式已过时
//    override val coroutineContext: CoroutineDispatcher
//        get() = Dispatchers.IO

    lateinit var deferred: List<Deferred<Int>>
    lateinit var datas: List<Int>

    override suspend fun doWork(): Result {
        /*return coroutineScope {
            Log.e("wcc", "doWork:  ${Thread.currentThread().name}")

            val time = measureTimeMillis {
                deferred = (0 until 100).map {
                    async {
                        delay(2000)
                        2
                    }
                }
                datas = deferred.awaitAll() //阻塞代码
            }

            Log.e("wcc", "${datas.sum()},  time: $time") //200,  time: 2041
            Result.success()
        }*/

        //可指定线程
        return withContext(Dispatchers.IO) {
            Log.e("wcc", "withContext:  ${Thread.currentThread().name}")

            val time = measureTimeMillis {
                deferred = (0..99).map {
                    async {
                        delay(2000)
                        3
                    }
                }
                datas = deferred.awaitAll()
            }
            Log.e("wcc", "${datas.sum()},  time: $time")
            Result.success()
        }
    }

    /*

        for (i in 0 until 100) {
            async {
                delay(200)
                1
            }
        }
        这种方式只能拿到声明了变量的任务结果，替换方式：
        public suspend fun <T> Collection<Deferred<T>>.awaitAll(): List<T>


        使用CoroutineWorker后，异步任务执行线程不再由Configuration设置的Excutor指定，而是由协程处理线程调度；
            默认是Dispatchers.Default，可以通过重写coroutineContext指定线程(但已过时)，或withContext指定线程;
     */

    companion object {
        fun start(context: Context) {
            val workRequest = OneTimeWorkRequestBuilder<CoroutineDownloadWorker>()
                .build()
            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }

}