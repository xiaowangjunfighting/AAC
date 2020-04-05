package com.example.aacinaction.viewbinding
/*
    视图绑定：

        视图绑定某一Module后，系统会为每个xml生成一个绑定类；
            若需要忽略某个xml文件，在文件根元素中添加tools:viewBindingIgnore="true"；
            绑定类命名方式：xml文件名驼峰+Binding;
            绑定类会引用xml中有id的控件；

            获取一个绑定类的实例：调用绑定类的静态方法inflate；
            绑定类的getRoot方法，返回布局文件根元素的引用；

            优点：
                1，与findViewById相比，不存在视图ID无效导致的空指针异常；
                2，不需要进行类型转换，保证了类型的安全；

            视图绑定与数据绑定库区别：
                1，视图绑定不支持布局变量和布局表达式，不能将xml与数据进行绑定；
                2，数据绑定库仅处理<layout>代码创建的数据绑定布局；

 */

