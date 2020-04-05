package com.example.aacinaction.databinding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.aacinaction.R
import java.util.zip.Inflater

class TestActivity :AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityTestBinding>(this, R.layout.activity_test)
    }

}

class MyFragement : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var binding1 = ActivityTestBinding.inflate(inflater, container, false)
        val binding2 = DataBindingUtil.inflate<ActivityTestBinding>(inflater, R.layout.activity_test, container, false)

        return binding1.root
    }

}

/*
        使用工具类DataBindingUtil完成Activity的setContentView操作；

        每个绑定了数据的布局文件，都会生成一个类，类名：布局文件驼峰法+Binding;

        Fragment加载视图和列表控件需要得到一个View：可以使用ActivityTestBinding.inflate或DataBindingUtil.inflate；

     */