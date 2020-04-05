package com.example.aacinaction.navigation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.aacinaction.R
import com.example.aacinaction.databinding.ActivityNavBinding

class NavActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityNavBinding>(this, R.layout.activity_nav)
    }

    fun test(view: View) {
        //Safe Args实现类型安全的导航
        val action = NavFragment1Directions.actionNavFragment1ToNavFragment2()
        view.findNavController().navigate(action)


        //嵌套图表
         view.findNavController().navigate(R.id.navigation2)

        //全局操作
        view.findNavController().navigate(R.id.action_global_fragment3)
    }
}

/*
    View, Activity, Fragment都可以获取NavController；
    导航到目的地还需要Java支持：由NavController完成；
 */

class NavFragment1 : Fragment() {

}

class NavFragment2 : Fragment() {


}

class NavFragment3 : Fragment() {


}

class NavFragment4 : Fragment() {


}