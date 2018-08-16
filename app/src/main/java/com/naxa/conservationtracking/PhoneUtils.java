package com.naxa.conservationtracking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresPermission;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.naxa.conservationtracking.application.ApplicationClass;

import Utls.SharedPreferenceUtils;

import static android.Manifest.permission.READ_PHONE_STATE;
import static com.naxa.conservationtracking.database.DataBaseConserVationTracking.IMEI;

public class PhoneUtils {
    /**
     * Return the unique device id.
     * <p>Must hold
     * {@code <uses-permission android:name="android.permission.READ_PHONE_STATE" />}</p>
     *
     * @return the unique device id
     */
    @SuppressLint({"HardwareIds", "MissingPermission"})
    @RequiresPermission(READ_PHONE_STATE)
    public static String getDeviceId() {
        TelephonyManager tm =
                (TelephonyManager) ApplicationClass.getApp().getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null ? tm.getDeviceId() : "";
    }


    public static String getIdFromPref() {
        return SharedPreferenceUtils.getInstance(ApplicationClass.getApp().getApplicationContext()).getStringValue(IMEI, "");
    }

    public static String getFormatedId() {
        return "_" + getIdFromPref();
    }
}
