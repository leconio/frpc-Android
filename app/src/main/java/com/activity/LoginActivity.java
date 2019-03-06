package com.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.frpc.R;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {

    private EditText exServiceIp;//服务器ip
    private EditText exServicePort;//服务器端口号
    private EditText exServiceToken;//服务器登录token
    private Button btSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();//初始化控件
        initEvent();//加载事件
    }

    private void initView() {
        exServiceIp = findViewById(R.id.service_ip);
        exServicePort = findViewById(R.id.service_port);
        exServiceToken = findViewById(R.id.service_token);
        btSave = findViewById(R.id.save);
        fillTextView();
    }

    private void fillTextView() {
        readFromSp();
    }

    private void saveSp(String ip, String port, String token) {
        String ipInfo = TextUtils.join("|", new String[]{ip, port, token});
        SharedPreferences info = getSharedPreferences("config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = info.edit().putString("ipInfo", ipInfo);
        editor.apply();
    }

    private void readFromSp() {
        SharedPreferences info = getSharedPreferences("config", Context.MODE_PRIVATE);
        String ipInfo = info.getString("ipInfo", "");
        if (!TextUtils.isEmpty(ipInfo)) {
            String[] i = ipInfo.split("\\|");
            if (i.length == 3) {
                exServiceIp.setText(i[0]);
                exServicePort.setText(i[1]);
                exServiceToken.setText(i[2]);
            }
        }
    }

    private void initEvent() {
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip = exServiceIp.getText().toString();
                String port = exServicePort.getText().toString();
                String token = exServiceToken.getText().toString();
                saveSp(ip, port, token);
                if (port.length() > 0) {
                    int int_port = Integer.parseInt(port);
                    if (ip.length() > 2 && 65535 >= int_port && int_port >= 1024 && token.length() > 2) {
                        saveServerInfo(ip, port, token);
                        Intent intent = new Intent(LoginActivity.this, KnifeNetListActivity.class);
                        saveSp(ip, port, token);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "输入有误", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "输入有误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * @param service_ip
     * @param service_port
     * @param service_token 向配置文件中写入FRP服务器的参数
     */
    private void saveServerInfo(String service_ip, String service_port, String service_token) {
        FrpAndroid.serverConfig = "[common]\r\n" +
                "server_addr = " + service_ip + "\r\n" +
                "server_port = " + service_port + "\r\n" +
                "token = " + service_token + "\r\n" +
                "admin_addr = 0.0.0.0" + "\r\n" +
                "admin_port = 7400" + "\r\n" +
                "admin_user = admin" + "\r\n" +
                "admin_pwd = admin" + "\r\n" +
                "log_file = /storage/emulated/0/Android/data/com.frp.fun/files/frpc.log" + "\r\n" +
                "log_level = info" + "\r\n" +
                "log_max_days = 3" + "\r\n" +
                "pool_count = 5" + "\r\n" +
                "login_fail_exit = true" + "\r\n" +
                "protocol = tcp" + "\r\n";
    }
}

