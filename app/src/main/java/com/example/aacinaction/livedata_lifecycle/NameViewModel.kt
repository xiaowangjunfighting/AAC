package com.example.aacinaction.livedata_lifecycle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.aacinaction.data.User

class NameViewModel : ViewModel() {

    //create a liveData with a String
    val currentName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    //转换LiveData
    val sourceData = MutableLiveData<User>()

    //使用map转换
    val transformData1: LiveData<String> = Transformations.map(sourceData) { user ->
        "transformData1: " + user.name
    }

    //使用switchMap转换
    val transformData2: LiveData<String> = Transformations.switchMap(sourceData) { user ->
        //返回值必须是LiveData
        MutableLiveData<String>().apply {
            value = "transformData2: " + user.name
        }
    }

    /*
        sourceData更改了数据，transformData1,transformData2都会通知关联自己的观察者
     */

    //Rest of the ViewModel...
}