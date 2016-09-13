package cacard.androiddynamicloadapk_simpledemo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * 宿主程序的代理Activity。
 * 这个Activity就是个壳子，注入到Client中的Activity中，作为proxy，代理Client的Activity的具体执行。
 * <p/>
 * Client中的Activity要按一定规矩写，不然无法注入，无法控制；
 * <p/>
 * Created by cunqingli on 2016/9/13.
 */
public class ProxyActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() != null) {
            String className = getIntent().getStringExtra(DLConfig.KEY_CLIENT_ACTIVITY_NAME);
            launchTargetActivity(className);
        }
    }

    /**
     * 1，使用DexClassLoader加载Apk；
     * 2，反射创建要调起的Activity的对象实例（这个插件Activity要符合一定的要求，比如有个setProxy，且在有proxy注入后，要通过proxy执行相关方法）；
     * 3，注入当前的ProxyActivity对象给2中的Activity实例，相当于给了它一个上下文；
     * 4，通过反射调用Activity对象的onCreate()，启动这个插件Activity；
     *
     * @param className
     */
    private void launchTargetActivity(String className) {
        MyLog.log("->launchTargetActivity(), className:" + className);

        // 先不考虑className为空的情况
        if (TextUtils.isEmpty(className)) {
            Toast.makeText(this, "targetClassName is null", Toast.LENGTH_LONG);
            return;
        }

        // ****** 接下来就是DexClassLoader登场了 *********

        String dexPath = DLConfig.PLUGIN_APK_PATH;
        // DexClassLoader需要一个输出目录，存放apk优化后的文件，不允许放到sd卡了。
        String dexOptDir = getDir("dexopt", Context.MODE_PRIVATE).getAbsolutePath();


        try {
            // 使用DexClassLoader加载Apk
            DexClassLoader dexClassLoader = new DexClassLoader(dexPath, dexOptDir, null, getClassLoader());

            // 加载类型
            Class<?> clazz = dexClassLoader.loadClass(className);

            // 使用反射创建对象
            Constructor<?> ctor = clazz.getConstructor(new Class[]{});
            Object obj = ctor.newInstance(new Object[]{});

            // 注入proxy
            Method methodProxy = clazz.getDeclaredMethod("setProxy", new Class[]{Activity.class});
            methodProxy.invoke(obj, this);

            // 调起onCreate
            Method methodOnCreate = clazz.getDeclaredMethod("onCreate", new Class[]{Bundle.class});
            Bundle bundle = new Bundle();
            bundle.putInt("from", 0);// 表示从宿主过去的标记
            methodOnCreate.invoke(obj, new Object[]{bundle});

        } catch (Exception e) {
            e.printStackTrace();
            MyLog.log("error:" + e.getMessage());
        }

    }
}
