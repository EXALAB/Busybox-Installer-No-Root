package exa.free.security;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

public class PermissionVerifier {

    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE
    };

    private Context mContext;

    public PermissionVerifier(Context context) {
        mContext = context.getApplicationContext();
    }

    public String verifyApplication(){

        if(lacksPermissions(PERMISSIONS)){
            return "APPLICATION_MODIFIED";
        }else{
            return "PASSED";
        }
    }
    private boolean lacksPermissions(String... permissions) {
        for (String permission : permissions) {
            if(lacksPermission(permission)){
                return true;
            }
        }
        return false;
    }
    private boolean lacksPermission(String permission) {
        return ContextCompat.checkSelfPermission(mContext, permission) ==
                PackageManager.PERMISSION_DENIED;
    }
}
