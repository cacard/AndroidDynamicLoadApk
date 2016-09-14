package cacard.androiddynamicloadapk_client;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 这个直接使用动态创建的控件，而不是使用资源布局文件。
 * 宿主调起后应该可以正常显示。
 *
 * Created by cunqingli on 2016/9/13.
 */
public class SomeActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Client/SomeActivity");

        LinearLayout ll = new LinearLayout(this);
        TextView tv = new TextView(this);
        tv.setText("Clinet->SomeActivity");

        ll.addView(tv);

        setContentView(ll);
    }
}
