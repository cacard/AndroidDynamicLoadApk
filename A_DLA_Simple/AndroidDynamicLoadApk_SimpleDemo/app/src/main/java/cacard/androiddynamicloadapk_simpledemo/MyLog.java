package cacard.androiddynamicloadapk_simpledemo;

import android.text.TextUtils;
import android.util.Log;

/**
 * Created by cunqingli on 2016/9/13.
 */
public class MyLog {

    public static final String sTagDefault = "DLTag";

    public static void log(String msg) {
        log(null, msg);
    }

    public static void log(String tag, String msg) {
        tag = TextUtils.isEmpty(tag) ? sTagDefault : tag;
        Log.i(tag, msg);
    }

}
