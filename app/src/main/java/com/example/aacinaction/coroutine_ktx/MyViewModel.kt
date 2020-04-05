package com.example.aacinaction.coroutine_ktx

import androidx.lifecycle.*
import com.example.aacinaction.data.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyViewModel : ViewModel() {

    init {
        viewModelScope.launch {
            //ViewModel销毁后，系统会取消Coroutine

        }
    }


    val user: LiveData<User> = liveData {
        val user = loadUser()
        this.emit(user)
    }

    private val userId: LiveData<String> = MutableLiveData()

    val user1 = userId.switchMap {id ->
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(loadUser(id))
        }
    }

    /*
        emit方法会一直挂起，直到主线程使用了LiveData的值；

        每次调用emit和emitSource，都会移除之前添加的来源；
     */




    private fun loadUser(id: String = "id") : User{
        //async work
        return User("kobe", "bryant")
    }

}