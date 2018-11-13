package com.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.activity.greendao.DBSuiDaoHelper;
import com.frpc.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {

    private EditText Exservice_ip;//服务器ip
    private EditText Exservice_port;//服务器端口号
    private EditText Exservice_token;//服务器登录token
    private Button Btsave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();//初始化控件
        initEvent();//加载事件
    }
    private void initView(){
        Exservice_ip = findViewById(R.id.service_ip);
        Exservice_port = findViewById(R.id.service_port);
        Exservice_token = findViewById(R.id.service_token);
        Btsave = findViewById(R.id.save);
    }
    private void initEvent(){

        Btsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip = Exservice_ip.getText().toString();
                String port = Exservice_port.getText().toString();
                String token = Exservice_token.getText().toString();
                if(port.length()>0){
                    int int_port = Integer.parseInt(port);
                if(ip.length()>2 && 65535>=int_port &&int_port>=1024 && token.length()>2){
                    DBSuiDaoHelper.deleteALL();
                    copyToSD(ip,port,token);
                    Intent intent = new Intent(LoginActivity.this,knife_net_list.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(LoginActivity.this,"输入有误",Toast.LENGTH_SHORT).show();
                }

                }else{
                    Toast.makeText(LoginActivity.this,"输入有误",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * @param service_ip
     * @param service_port
     * @param service_token
     * 向配置文件中写入FRP服务器的参数
     */
    private void copyToSD(String service_ip,String service_port,String service_token) {
        InputStream in = null;
        FileOutputStream out = null;

        //判断如果数据库已经拷贝成功，不需要再次拷贝
        File file = new File(this.getExternalFilesDir(null), MyApplication.FILENAME);
        if(file.exists()){
            file.delete();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
                AssetManager assets = getAssets();
                //2.读取数据资源
                in = assets.open(MyApplication.FILENAME);
                out = new FileOutputStream(file);
                //3.读写操作
                byte[] b = new byte[1024];//缓冲区域
                int len = -1; //保存读取的长度
                while ((len = in.read(b)) != -1) {
                    out.write(b, 0, len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            out = new FileOutputStream(file,true);
                String common = "[common]\r\n";
                String server_addr = "server_addr = "+service_ip+"\r\n";
                String server_port = "server_port = "+ service_port + "\r\n";
                String token = "token = "+ service_token + "\r\n";
                String admin_addr = "admin_addr = 0.0.0.0"+"\r\n";
                String admin_port = "admin_port = 7400"+"\r\n";
                String admin_user = "admin_user = admin"+"\r\n";
                String admin_pwd = "admin_passwd = admin"+"\r\n";
                String log_file = "log_file = /storage/emulated/0/Android/data/com.frp.fun/files/frpc.log"+"\r\n";
                String log_level = "log_level = info"+"\r\n";
                String log_max_days = "log_max_days = 3"+"\r\n";
                String pool_count = "pool_count = 5"+"\r\n";
                String tcp_mux = "tcp_mux = true"+"\r\n";
                String login_fail_exit = "login_fail_exit = true"+"\r\n";
                String protocol = "protocol = tcp"+"\r\n";
                out.write(common.getBytes());
                out.write(server_addr.getBytes());
                out.write(server_port.getBytes());
                out.write(token.getBytes());
                out.write(admin_addr.getBytes());
                out.write(admin_port.getBytes());
                out.write(admin_user.getBytes());
                out.write(admin_pwd.getBytes());
                out.write(log_file.getBytes());
                out.write(log_level.getBytes());
                out.write(log_max_days.getBytes());
                out.write(pool_count.getBytes());
                out.write(tcp_mux.getBytes());
                out.write(login_fail_exit.getBytes());
                out.write(protocol.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

