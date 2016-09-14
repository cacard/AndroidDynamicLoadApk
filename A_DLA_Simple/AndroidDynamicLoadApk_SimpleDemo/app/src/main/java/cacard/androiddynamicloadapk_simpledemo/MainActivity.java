package cacard.androiddynamicloadapk_simpledemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 这个Activity是测试入口，他是宿主（Host）的普通Activity，
 * 不觉察到插件（Client）的任何信息。
 * 插件的调起完全交给Proxy（ProxyActivity）。
 * <p>
 * Created by cunqingli on 2016/9/13.
 */
public class MainActivity extends Activity implements View.OnClickListener {

    int btnId = 0;

    /**
     * 要调起的Client中哪个Activity？使用包路名名
     */
    static final String targetActivityInClient = "cacard.androiddynamicloadapk_client.MainActivity";

    // test
    public MainActivity() {
        super();
        MyLog.log("->MainActivty#ctor()");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout ll = new LinearLayout(this);

        Button btn = new Button(this);
        btn.setId(btnId = View.generateViewId());
        btn.setText("Invoke Plugin's Activity");
        btn.setOnClickListener(this);
        ll.addView(btn);

        setContentView(ll);
    }

    @Override
    public void onClick(View view) {
        if (view != null) {
            if (view.getId() == btnId) {
                MyLog.log("clicked. will start activity:" + targetActivityInClient);
                Intent intent = new Intent(MainActivity.this, ProxyActivity.class);
                intent.putExtra(DLConfig.KEY_CLIENT_ACTIVITY_NAME, targetActivityInClient);
                MainActivity.this.startActivity(intent);
            }
        }
    }
}
