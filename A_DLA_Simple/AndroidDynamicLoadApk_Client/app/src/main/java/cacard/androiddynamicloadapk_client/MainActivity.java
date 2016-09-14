package cacard.androiddynamicloadapk_client;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * 插件的Activity是需要一定限制（格式、代码调用）的。
 * 而不是说任意apk文件都可以被宿主（Host）正常调起。
 * <p/>
 * Created by cunqingli on 2016/9/13.
 */
public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 使用布局文件
        setContentView(R.layout.activity_main);
        setTitle("Client/MainActivity");
    }
}
