package com.example.aacinaction.coroutine_ktx

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.PrecomputedTextCompat
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.example.aacinaction.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ActivityMainBinding.inflate(layoutInflater).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch(Dispatchers.Main) {
            val params = TextViewCompat.getTextMetricsParams(nameTextView)
            withContext(Dispatchers.Default) {
                PrecomputedTextCompat.create("abc", params)
            }.also {
                TextViewCompat.setPrecomputedText(nameTextView, it)
            }

        }

        viewLifecycleOwner.lifecycleScope.launch {

        }

        //最终都是调用这个
        lifecycle.coroutineScope.launch {

        }

        /*
            可以通过以上3种方式访问LifeCycle的CoroutineScope

            Lifecycle销毁，协程也会取消；
         */
    }

    //只要考虑到Lifecycle的状态，可以在初始化构造器的时候做一些事
    init {
        lifecycleScope.launch(Dispatchers.Main) {
            //方式1

            //当Lifecycle的状态至少为STARTED时才执行
            whenStarted {
                //update UI

                withContext(Dispatchers.IO) {
                    //handle async work
                }

                //run fragment transactions safely
            }


            whenCreated {

            }

            whenResumed {

            }
        }

        //方式2
        lifecycleScope.launchWhenStarted {

        }

        lifecycleScope.launchWhenCreated {

        }

        lifecycleScope.launchWhenResumed {

        }
    }

    /*
        某些情况下协程内代码执行的时机，可能需要考虑Lifecycle的状态；

        若协程内代码运行过程中，Lifecycle不满足条件了，那么会暂停执行协程内的代码；

        协程不会随着Activity的重启而重启，它只跟Lifecycle有关；
     */


}