package cacard.androiddynamicloadapk_client;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

/**
 * 由于每个插件的Activity都需要一定的规范，所以有个BaseActivity。
 * <p>
 * Created by cunqingli on 2016/9/13.
 */
public class BaseActivity extends Activity {

    /**
     * Activity代理
     */
    Activity mProxy;

    /**
     * From标记
     * 如果从宿主过来，通过代理执行；
     * 如果从内部过来（就是说没有宿主，而是安装插件apk后自己执行），正常执行；
     */
    public static final int FROM_HOST = 0;
    public static final int FROM_INTERNAL = 1;
    private int mFrom = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mFrom = savedInstanceState.getInt("from", FROM_INTERNAL);
        }

        if (mFrom == FROM_INTERNAL) {
            super.onCreate(savedInstanceState);
            mProxy = this;
        }

        if (mFrom == FROM_HOST) {
            // ?
        }
    }

    public void setProxy(Activity proxy) {
        mProxy = proxy;
    }

    /**
     * **********************************
     * 改造setContentView
     * 子类（具体插件的Activity）在setContentView时，就会根据有无proxy执行相应的动作
     ***********************************/
    @Override
    public void setContentView(int layoutResID) {
        if (mProxy == this) {
            super.setContentView(layoutResID);
        } else {
            mProxy.setContentView(layoutResID);
        }
    }

    @Override
    public void setContentView(View view) {
        if (mProxy == this) {
            super.setContentView(view);
        } else {
            mProxy.setContentView(view);
        }
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if (mProxy == this) {
            super.setContentView(view, params);
        } else {
            mProxy.setContentView(view, params);
        }
    }
}
