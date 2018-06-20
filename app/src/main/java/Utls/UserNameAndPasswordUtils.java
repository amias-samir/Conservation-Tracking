package Utls;

import android.content.Context;
import android.text.TextUtils;

import com.naxa.conservationtracking.climate_change.BiogasDetail;

import java.util.ArrayList;
import java.util.List;

public class UserNameAndPasswordUtils {

    public static List<String> getUserNameAndPassword(Context context){
        List<String > userNameAndPassword = new ArrayList<String>();

        SharedPreferenceUtils sharedPreferenceUtils = new SharedPreferenceUtils(context);

        userNameAndPassword.add((TextUtils.isEmpty(sharedPreferenceUtils.getStringValue(SharedPreferenceUtils.KEY_USER_NAME, null)) ?
                SharedPreferenceUtils.KEY_DEFAULT_USER_NAME : sharedPreferenceUtils.getStringValue(SharedPreferenceUtils.KEY_USER_NAME, null)));

        userNameAndPassword.add(TextUtils.isEmpty(sharedPreferenceUtils.getStringValue(SharedPreferenceUtils.KEY_USER_PASSWORD, null)) ?
                SharedPreferenceUtils.KEY_DEFAULT_USER_PASS : sharedPreferenceUtils.getStringValue(SharedPreferenceUtils.KEY_USER_PASSWORD, null));

        return userNameAndPassword;
    }
}
