package com.example.aacinaction.livedata_lifecycle

import android.app.Activity
import android.content.Context
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.*
import com.example.aacinaction.R
import com.example.aacinaction.data.User
import com.example.aacinaction.databinding.ActivityMainBinding
import com.example.aacinaction.utils.StockManager
import kotlinx.android.synthetic.main.activity_main.*
import java.math.BigDecimal

class MainActivity : AppCompatActivity() {

    private lateinit var listener: MyLocationListener
    private lateinit var viewModel: NameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        //Lifecycle
        listener =
            MyLocationListener(
                this,
                lifecycle
            ) { location ->
                //update UI
            }
        lifecycle.addObserver(listener)


        //LiveData
        viewModel = ViewModelProvider(this).get(NameViewModel::class.java)
        viewModel.currentName.observe(this, Observer {str ->
            //update UI
            binding.nameTextView.text = str
        })

        /*
            更新LiveData中数据，并回调onChanged方法通知Observer；
            主线程用setValue，异步线程用postValue；
         */
        binding.nameTextView.setOnClickListener {
            viewModel.currentName.value = "click"
        }

        /*
            LiveData具有感知生命周期的能力：
                1，Lifecycle对象处于未活跃状态，即便值发生改变，也不会通知观察者；
                2，Lifecycle对象销毁后，会自动移除观察者；

            可以在Activity，Fragment, Service之间共享LiveData，可以将LiveData实现为单例；
         */

        //-------------------------------

        binding.button.setOnClickListener{
            //更改sourceData的值
            viewModel.sourceData.value = User("kobe", "bryant1")
        }

        viewModel.sourceData.observe(this, Observer {
            Log.e("wcc", "sourceData ${it.name}") //sourceData kobe
        })

        viewModel.transformData1.observe(this, Observer {
            Log.e("wcc", "transformData1 $it") //transformData1 transformData1: kobe
        })

        viewModel.transformData2.observe(this, Observer {
            Log.e("wcc", "transformData2 $it") //transformData2 transformData2: kobe
        })

    }

    //自定义LiveData
    class MyLiveData(symbol: String) : LiveData<BigDecimal>() {
        private val stockManager = StockManager()

        private val listener = { price: BigDecimal ->
            //更新值并通知活跃的观察者
            value = price
        }

        //LiveData有活跃的观察者时被回调
        override fun onActive() {
            super.onActive()
            stockManager.requestPriceUpdates(listener)
        }

        //LiveData没有活跃的观察者时被回调
        override fun onInactive() {
            super.onInactive()
            stockManager.removeUpdates(listener)
        }

        companion object {
            private lateinit var INSTANCE: MyLiveData

            fun getIntance(symbol: String) : MyLiveData {
                INSTANCE = if (Companion::INSTANCE.isInitialized) INSTANCE else MyLiveData(
                    symbol
                )
                return INSTANCE
            }
        }
    }

    override fun onStart() {
        super.onStart()
//        listener.start()
    }

    override fun onStop() {
        super.onStop()
//        listener.start()
    }

    internal class MyLocationListener(
        private val context: Context,
        private val lifecycle: Lifecycle,
        private val callback: (Location) -> Unit
    ) : LifecycleObserver {

        private var enable = false

        fun enable() {
            enable = true
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {

            }
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        fun start() {
            Log.e("wcc", "start")
            if (enable) {
                //connect to system location service
            }
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        fun stop() {
            Log.e("wcc", "stop")
            //disconnect if connected
        }
    }

}

/*
    Lifecycle:
        解决的问题：
            1，真实的开发场景中，会有很多管理界面和其他组件的调用。在Activity生命周期存放大量的代码，维护更加困难；
            2，无法保证其他组件在Activity停止之前启动完成；
                例如：onStart中启动一个组件花费时间较长，在onStart方法执行完成前，onStop已执行结束；

        优点：
            对Activity生命周期变化作出响应的逻辑，放在了LifecycleObserver中，而非放在Activity中；

        步骤：
            1，其他组件实现LifeCycleObserver(在方法上使用注解@OnLifecyleEvent，管理其他组件的声明周期)
            2，Activity或Fragment实现LifecycleOwner
            3，在Activity中添加观察者Observer,调用Lifecyle$addObserver方法

        注意：
        26.1.0+版本的Activity和Fragment已实现了LifecycleOwner；
            若自定义的活动页继承了AppCompatActivity，则无需处理LifecycleOwner;
            若继承的是Activity，则需要配合LifecycleRegistry自定义实现了LifecycleOwner的Activity;
 */

//自定义LifecycleOwner
class MyActivity : Activity(), LifecycleOwner {
    private lateinit var lifecycleRegistry: LifecycleRegistry

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleRegistry = LifecycleRegistry(this)
        lifecycleRegistry.markState(Lifecycle.State.CREATED)
    }

    override fun onStart() {
        super.onStart()
        lifecycleRegistry.markState(Lifecycle.State.STARTED)
    }

    override fun getLifecycle(): Lifecycle = lifecycleRegistry
}
