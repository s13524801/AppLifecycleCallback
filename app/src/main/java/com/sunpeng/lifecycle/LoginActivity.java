package com.sunpeng.lifecycle;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class LoginActivity extends Activity {

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void loginAction(View view) {
        Log.e(TAG, "TopActivity --> " + ActivityStackManager.getInstance().getTopActivity());
        Log.e(TAG, "CurrentActivity --> " + ActivityStackManager.getInstance().getCurrentActivity());
    }
}
