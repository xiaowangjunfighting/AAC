package com.example.aacinaction.viewmodel

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import com.example.aacinaction.databinding.ActivityMainBinding

class MyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityMainBinding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(activityMainBinding.root)

        /*val viewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        viewModel.getUsers().observe(this, Observer {data ->
            //update UI
        })*/

        //---测试系统配置发生更改后，ViewModel可以保存并恢复数据

        val viewModel = ViewModelProvider(this, SavedStateViewModelFactory(application, this))
            .get(SavedStateHandleVM::class.java)

        viewModel.data.observe(this, Observer {
            Log.e("wcc", it)
        })

        //1，点击按钮修改ViewModel值；2，旋转屏幕；3，ViewModel值仍为second
        activityMainBinding.button.setOnClickListener {
            viewModel.data.value = "second"
        }


        //---测试应用进程被系统杀死后（开发者选项中模拟），SavesStateHandle可以恢复并保存数据

        val liveData = viewModel.handle.getLiveData<String>(SavedStateHandleVM.KEY)
        liveData.observe(this, Observer {
//            Log.e("wcc", it)
        })

        //1，点击按钮修改ViewModel值；2，杀死应用进程并重启Activity；3，ViewModel值仍为SavedStateHandle 2
        activityMainBinding.button1.setOnClickListener {
            viewModel.set("SavedStateHandle 2")
        }
    }

}