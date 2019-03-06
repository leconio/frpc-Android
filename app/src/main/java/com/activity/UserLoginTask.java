package com.activity;

import android.os.AsyncTask;
import android.util.Log;

import frpclib.Frpclib;

/**
 * 启动Frp
 */
public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

    private final String mConfigPath;

    UserLoginTask(String configPath) {
        mConfigPath = configPath;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            Frpclib.run(mConfigPath);
        } catch (Throwable e) {
            Log.e("throwable", e.getMessage() + "");
        }
        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        //由于Frpclib.run(mConfigPath)该方法保持了长连接，所以这些方法都走不进去，只是摆设
        Log.d(FrpAndroid.TAG, String.valueOf(success));
    }

    @Override
    protected void onCancelled() {
        //由于Frpclib.run(mConfigPath)该方法保持了长连接，所以这些方法都走不进去，只是摆设，但退出程序会走这个方法
        Log.e(FrpAndroid.TAG, "cancel");
    }
}