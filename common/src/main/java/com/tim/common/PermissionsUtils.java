package com.tim.common;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class PermissionsUtils {
    private static int getTargetSdkVersion(Activity activity) {
        int targetSdkVersion = 0;
        Context context = activity.getApplicationContext();
        try {
            final PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            targetSdkVersion = info.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return targetSdkVersion;
    }

    private static boolean checkRuntimePermissionAvailable(Activity activity) {
        // Always return true for API version < M, let the system deal with the permissions
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Log.w("Permission", "API version < M, return true by default");
            return false;
        }
        // Always return true for target sdk version < M, let the system deal with the permissions
        if (getTargetSdkVersion(activity) < Build.VERSION_CODES.M) {
            Log.w("Permission", "Target sdk version < M, return true by default");
            return false;
        }
        return true;
    }

    private static String[] getDeniedPermissions(Activity activity, String[] permissions, boolean isAll) {
        List<String> deniedPermissionList = new ArrayList<>();
        Context context = activity.getApplicationContext();
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                Log.i("Permission", "\"" + permission + "\" has been granted");
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    deniedPermissionList.add(permission);
                    Log.w("Permission", "\"" + permission + "\" was denied");
                } else {
                    if (isAll) {
                        deniedPermissionList.add(permission);
                    }
                    Log.w("Permission", "\"" + permission + "\" was denied completely");
                }
            }
        }

        String[] deniedPermissions = new String[deniedPermissionList.size()];
        deniedPermissions = deniedPermissionList.toArray(deniedPermissions);

        return deniedPermissions;
    }

    //    private static
    public interface OnRequestPermission {
        void onSuccess();

        void onFailed();
    }

    private static OnRequestPermission onRequestPermission;

    public static boolean requestPermissions(Activity activity, int requestCode, OnRequestPermission onRequestPermission, String... deniedPermissions) {
        PermissionsUtils.onRequestPermission = onRequestPermission;
        for (String permission : deniedPermissions) {
            if (!(ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(activity, deniedPermissions, requestCode);
                return false;
            }
        }
        if (onRequestPermission != null) {
            onRequestPermission.onSuccess();
        }
        return true;
    }

    public static void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        boolean isAllGranted = true;
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        isAllGranted = false;
                        break;
                    }
                }



        if (isAllGranted) {
            // All permissions are granted
            Log.i("Permission", "All permissions have been granted");
            if (onRequestPermission != null) {
                onRequestPermission.onSuccess();
            }
        } else {
            // There is at least a permission which aren't granted
            Log.w("Permission", "There is at least a permission which aren't granted");
            if (onRequestPermission != null) {
                onRequestPermission.onFailed();
            }
        }
    }
}
