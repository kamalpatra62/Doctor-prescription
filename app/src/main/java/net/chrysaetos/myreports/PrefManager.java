package net.chrysaetos.myreports;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "myreports";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    private  static final String IS_LAST_VIST_PAGE ="isLastVistPage";

    private  static final String IS_LOGIN ="isLogin";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }
    public void setLastvistPage(boolean isLastPage)
    {
        editor.putBoolean(IS_LAST_VIST_PAGE, isLastPage);
        editor.commit();
    }
    public void setLogin(boolean isLogin)
    {
        editor.putBoolean(IS_LOGIN, isLogin);
        editor.commit();
    }
    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public boolean isLastvistPage() {
        return pref.getBoolean(IS_LAST_VIST_PAGE, true);
    }
    public boolean isLogIN() {
        return pref.getBoolean(IS_LOGIN, true);
    }

}

