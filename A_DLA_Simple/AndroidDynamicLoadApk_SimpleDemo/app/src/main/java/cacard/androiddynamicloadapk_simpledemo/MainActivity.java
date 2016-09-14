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
 * <p/>
 * Created by cunqingli on 2016/9/13.
 */
public class MainActivity extends Activity implements View.OnClickListener {

    int btnId1 = 0;
    int btnId2 = 0;

    /**
     * 要调起的Client中哪个Activity？使用包路名名
     */
    static final String targetActivityInClient1 = "cacard.androiddynamicloadapk_client.SomeActivity";
    static final String targetActivityInClient2 = "cacard.androiddynamicloadapk_client.MainActivity";

    // test
    public MainActivity() {
        super();
        MyLog.log("->MainActivty#ctor()");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);

        Button btn = new Button(this);
        btn.setId(btnId1 = View.generateViewId());
        btn.setText("Invoke Plugin's Activity:SomeActivity Using code View");
        btn.setOnClickListener(this);

        Button btn2 = new Button(this);
        btn2.setId(btnId2 = View.generateViewId());
        btn2.setText("Invoke Plugin's Activity:MainActivity Using xml");
        btn2.setOnClickListener(this);

        ll.addView(btn);
        ll.addView(btn2);

        setContentView(ll);
    }

    @Override
    public void onClick(View view) {
        if (view != null) {

            String target = "";

            if (view.getId() == btnId1) {
                target = targetActivityInClient1;
            }

            if (view.getId() == btnId2) {
                target = targetActivityInClient2;
            }

            MyLog.log("clicked. will start activity:" + target);
            Intent intent = new Intent(MainActivity.this, ProxyActivity.class);
            intent.putExtra(DLConfig.KEY_CLIENT_ACTIVITY_NAME, target);
            MainActivity.this.startActivity(intent);
        }
    }
}
