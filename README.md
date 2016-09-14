# AndroidDynamicLoadApk

学习dynamic-load-apk写的demo

核心原理
- DexClassLoader动态加载插件apk；
- 反射创建插件apk中的Activity对象，并注入宿主的一个代理（ProxyActivity）到插件的Activity对象中；
- 插件Activity通过注入的proxyActivity执行生命周期；

问题
- 插件无法通过自己的layout渲染布局，只能动态；即插件无法使用自己的资源文件。
