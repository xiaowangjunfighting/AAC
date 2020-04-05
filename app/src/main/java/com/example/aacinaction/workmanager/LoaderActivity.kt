package com.example.aacinaction.workmanager

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.aacinaction.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class LoaderActivity :AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

//        val task = Task()
//        task.start(this)
//        Log.e("wcc", "start")

//        CoroutineDownloadWorker.start(this)


        GlobalScope.launch(Dispatchers.Main) {

            withContext(Dispatchers.Default) {
                //DefaultDispatcher-worker-1
                Log.e("wcc", "Default1:  ${Thread.currentThread().name}")
            }.apply {
                Log.e("wcc", "Default2:  ${Thread.currentThread().name}") //main
            }
            Log.e("wcc", "Default3:  ${Thread.currentThread().name}") //main

            //main
            withContext(Dispatchers.Main) {
                Log.e("wcc", "Main:  ${Thread.currentThread().name}")
            }

            //DefaultDispatcher-worker-1
            withContext(Dispatchers.IO) {
                Log.e("wcc", "IO:  ${Thread.currentThread().name}")
            }
        }
    }
}