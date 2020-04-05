package com.example.aacinaction.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.aacinaction.data.User

class MyViewModel() : ViewModel() {
    private val users: MutableLiveData<List<User>> by lazy {
        MutableLiveData<List<User>>().also {
            loadUser()
        }
    }

    fun getUsers(): LiveData<List<User>> {
        return users
    }

    private fun loadUser() {
        //do an asynchronous operation to fetch users
    }

    /*
        如果Activity重新创建了，它接受的ViewModel与之前Activity实例接受的ViewModel相同；

        ViewModel对象存在时间比LifecycleOwner(AppCompatActivity)长，它不能引用视图；
            ViewModel存在的时间范围：首次请求ViewModel到Activity完成并销毁；

        未验证：
        ViewModel需要获取Application，可以使用AndroidViewModel，并在MyViewModel构造方法上接受Application;

        Fragment之间共享数据，可以通过共享Activity的ViewModel来通信；
    */
}


class SavedStateHandleVM(val handle: SavedStateHandle) : ViewModel() {

    companion object {
        const val KEY = "key"
    }

    val data: MutableLiveData<String> by lazy {
        MutableLiveData<String>("first")
    }

    init {
        handle.getLiveData<String>(KEY, "SavedStateHandle 1")
    }

    fun set(value: String) {
        handle.getLiveData<String>(KEY).value = value
    }

}
