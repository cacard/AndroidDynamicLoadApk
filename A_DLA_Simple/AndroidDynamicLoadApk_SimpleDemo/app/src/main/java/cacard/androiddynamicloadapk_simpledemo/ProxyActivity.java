package cacard.androiddynamicloadapk_simpledemo;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.File;
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
        // 检查一下是否存在
        File fApk = new File(dexPath);
        if (fApk.exists() == false) {
            MyLog.log("error:client not exist");
            return;
        } else {
            String p = fApk.getAbsolutePath();
            String s = fApk.getName();
            boolean read = fApk.canRead();
            if (!read) {
                MyLog.log("error:client can't read");
                // 需要加上读取sd卡权限；
                return;
            }
        }

        // 看一下packageInfo
        PackageInfo pi = getPackageManager().getPackageArchiveInfo(dexPath, PackageManager.GET_ACTIVITIES);
        if (pi != null && pi.activities != null && pi.activities.length > 0) {
            for (int i = 0; i < pi.activities.length; i++) {
                String activityName = pi.activities[i].name;
            }
        }

        // DexClassLoader需要一个输出目录，存放apk优化后的文件，不允许放到sd卡了。
        String dexOptDir = getDir("dexopt", Context.MODE_PRIVATE).getAbsolutePath();
        
        try {
            // 使用DexClassLoader加载Apk
            ClassLoader clPath = getClassLoader(); // 使用这个不行
            ClassLoader clSys = ClassLoader.getSystemClassLoader();
            DexClassLoader dexClassLoader = new DexClassLoader(dexPath, dexOptDir, null, clSys);

            // 加载类型
            Class<?> clazz = dexClassLoader.loadClass(className);

            // 使用反射创建对象
            Constructor<?> ctor = clazz.getConstructor(new Class[]{});
            Object obj = ctor.newInstance(new Object[]{});

            // 注入proxy
            Method methodProxy = clazz.getMethod("setProxy", new Class[]{Activity.class});
            methodProxy.invoke(obj, this);

            // 调起onCreate
            Method methodOnCreate = clazz.getDeclaredMethod("onCreate", new Class[]{Bundle.class});
            methodOnCreate.setAccessible(true);
            Bundle bundle = new Bundle();
            bundle.putInt("from", 0);// 表示从宿主过去的标记
            methodOnCreate.invoke(obj, new Object[]{bundle});

        } catch (Exception e) {
            e.printStackTrace();
            MyLog.log("error:" + e.getMessage());
        }

    }


    @Override
    public void setContentView(int layoutResID) {

        MyLog.log("->ProxyActivity#setContentView()");

        //插件会调用到这里，但其实布局文件找不到；
        super.setContentView(layoutResID);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
    }
}
