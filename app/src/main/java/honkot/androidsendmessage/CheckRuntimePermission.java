package honkot.androidsendmessage;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;
import android.util.Log;

/**
 * Created by hiroki on 2016-11-07.
 */
public class CheckRuntimePermission {
    private final String TAG = "CheckRuntimePermission";
    private final int REQUEST_PERMISSION = 1000;

    private String[] PERMISSION = {
            Manifest.permission.SEND_SMS,
    };

    public boolean checkPermission(Activity activity) {
        // available over Android 6.0
        boolean ret = true;
        if (Build.VERSION.SDK_INT >= 23) {
            for (int i = 0; i < PERMISSION.length; i++) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, PERMISSION[i])) {
                    Log.i(TAG, "must permit : " + PERMISSION[i]);
                }
                if (ActivityCompat.checkSelfPermission(
                        activity, PERMISSION[i]) != PackageManager.PERMISSION_GRANTED) {
                    requestPermission(activity);
                    ret = false;
                }
            }
        }

        return ret;
    }

    private void requestPermission(Activity activity) {
        for (int i = 0; i < PERMISSION.length; i++) {
            ActivityCompat.requestPermissions(
                    activity, PERMISSION, REQUEST_PERMISSION);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            for (int i = 0; i < PERMISSION.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    Log.w(TAG, "User denied allow permission : " + PERMISSION[i]);
                }
            }
        }
    }
}
