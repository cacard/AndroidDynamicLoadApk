package cacard.androiddynamicloadapk_client;

import android.app.Activity;
import android.os.Bundle;

/**
 * 插件的Activity是需要一定限制（格式、代码调用）的。
 * 而不是说任意apk文件都可以被宿主（Host）正常调起。
 *
 * Created by cunqingli on 2016/9/13.
 */
public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
