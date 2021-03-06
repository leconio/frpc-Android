package com.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.activity.greendao.DBSuiDaoHelper;
import com.activity.greendao.SuiDao;
import com.frpc.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class KnifeNetActivity extends Activity {
    private RadioGroup typeRG;
    private EditText nameEx;
    private EditText IPEx;
    private EditText portEx;
    private EditText remote_portEx;
    private EditText domain_nameEx;
    private CheckBox compressCB;
    private CheckBox secretCB;
    private Button saveBt;
    private LinearLayout yumingLy;//域名
    private LinearLayout domain_type_layoutLy;//域名类型
    private LinearLayout remote_port_ly;
    private RadioGroup access_type_rg;
    private String suidaoType;
    private String name;//隧道名称
    private String local_ip;
    private String local_port;
    private String remote_port;
    private String domain_name;
    private String is_secret;
    private String is_compress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knife_net);
        initView();
        initEvent();
    }

    private void initView() {
        typeRG = findViewById(R.id.type_radio);
        nameEx = findViewById(R.id.name);
        IPEx = findViewById(R.id.accesss_ip);
        portEx = findViewById(R.id.port);
        remote_portEx = findViewById(R.id.remote_port);
        access_type_rg = findViewById(R.id.access_type_rg);
        yumingLy = findViewById(R.id.yuming);
        domain_type_layoutLy = findViewById(R.id.domain_type_layout);
        remote_port_ly = findViewById(R.id.remote_port_ly);
        domain_nameEx = findViewById(R.id.domain_name);
        compressCB = findViewById(R.id.compress);
        secretCB = findViewById(R.id.secret);
        saveBt = findViewById(R.id.save);
    }

    private void initEvent() {
        yumingLy.setVisibility(View.GONE);
        domain_type_layoutLy.setVisibility(View.GONE);
        typeRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.tcp:
                        suidaoType = "tcp";
                        yumingLy.setVisibility(View.GONE);
                        domain_type_layoutLy.setVisibility(View.GONE);
                        remote_port_ly.setVisibility(View.VISIBLE);
                        break;
                    case R.id.udp:
                        suidaoType = "udp";
                        yumingLy.setVisibility(View.GONE);
                        domain_type_layoutLy.setVisibility(View.GONE);
                        remote_port_ly.setVisibility(View.VISIBLE);
                        break;
                    case R.id.http:
                        suidaoType = "http";
                        yumingLy.setVisibility(View.VISIBLE);
                        domain_type_layoutLy.setVisibility(View.VISIBLE);
                        remote_port_ly.setVisibility(View.GONE);
                        break;
                    case R.id.https:
                        suidaoType = "https";
                        yumingLy.setVisibility(View.VISIBLE);
                        domain_type_layoutLy.setVisibility(View.VISIBLE);
                        remote_port_ly.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
            }
        });


        saveBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameEx.getText().length() > 1 && IPEx.getText().length() > 1 && portEx.getText().length() > 1) {
                    if (typeRG.getCheckedRadioButtonId() == R.id.tcp || typeRG.getCheckedRadioButtonId() == R.id.udp) {
                        if (remote_portEx.getText().length() < 2) {
                            Toast.makeText(KnifeNetActivity.this, "请完善信息", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else {
                        if (domain_nameEx.getText().length() < 2) {
                            Toast.makeText(KnifeNetActivity.this, "请完善信息", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }


                    suidaoInfo();//总和隧道信息
                    save();
                    finish();
                } else {
                    Toast.makeText(KnifeNetActivity.this, "请完善信息", Toast.LENGTH_SHORT).show();
                }

            }

            private void save() {
                new Thread() {
                    @Override
                    public void run() {
                        if (!DBSuiDaoHelper.haveSameSetionName(name)) {
                            copyToSD(name + "-" + FrpAndroid.FILENAME);//写入数据库
                            addsuidao(suidaoType, local_ip, local_port, remote_port, name, domain_name);//写入配置文件
                        }
                    }
                }.start();
            }
        });
    }

    private void suidaoInfo() {
        switch (typeRG.getCheckedRadioButtonId()) {
            case R.id.tcp:
                suidaoType = "tcp";
                break;
            case R.id.udp:
                suidaoType = "udp";
                break;
            case R.id.http:
                suidaoType = "http";
                break;
            case R.id.https:
                suidaoType = "https";
                break;
            default:
                suidaoType = "tcp";
                break;
        }
        name = nameEx.getText().toString();
        local_ip = IPEx.getText().toString();
        local_port = portEx.getText().toString();
        remote_port = remote_portEx.getText().toString();
        domain_name = domain_nameEx.getText().toString();
        if (compressCB.isChecked()) {
            is_compress = "true";
        } else {
            is_compress = "false";
        }
        if (secretCB.isChecked()) {
            is_secret = "true";
        } else {
            is_secret = "false";
        }
    }

    private void copyToSD(String dbName) {
        InputStream in = null;
        FileOutputStream out = null;

        //判断如果数据库已经拷贝成功，不需要再次拷贝
        File file = new File(this.getExternalFilesDir(null), dbName);
        File logfile = new File(this.getExternalFilesDir(null), "/" + name + "frpc.log");

        try {
            logfile.createNewFile();
            out = new FileOutputStream(file, true);
            out.write(FrpAndroid.getServerConfig(name).getBytes());
            switch (suidaoType) {
                case "tcp": {
                    String socket_name = "[" + name + "]\n";
                    String socket_type = "type = " + suidaoType + "\r\n";
                    String socket_ip = "local_ip = " + local_ip + "\r\n";
                    String socket_port = "local_port =  " + local_port + "\r\n";
                    String socket_remoteport = "remote_port = " + remote_port + "\r\n";
                    String use_encryption = "use_encryption = " + is_secret + "\r\n";
                    String use_compression = "use_compression = " + is_compress + "\r\n";
                    out.write(socket_name.getBytes());
                    out.write(socket_type.getBytes());
                    out.write(socket_ip.getBytes());
                    out.write(socket_port.getBytes());
                    out.write(socket_remoteport.getBytes());
                    out.write(use_encryption.getBytes());
                    out.write(use_compression.getBytes());
                    break;
                }
                case "http":
                    if (access_type_rg.getCheckedRadioButtonId() == R.id.custom) {
                        String socket_name = "[" + name + "]\n";
                        String socket_type = "type = " + suidaoType + "\r\n";
                        String socket_ip = "local_ip = " + local_ip + "\r\n";
                        String socket_port = "local_port =  " + local_port + "\r\n";
                        String socket_subdomain = "custom_domains = " + domain_name + "\r\n";
                        String use_encryption = "use_encryption = " + is_secret + "\r\n";
                        String use_compression = "use_compression = " + is_compress + "\r\n";
                        out.write(socket_name.getBytes());
                        out.write(socket_type.getBytes());
                        out.write(socket_ip.getBytes());
                        out.write(socket_port.getBytes());
                        out.write(socket_subdomain.getBytes());
                        out.write(use_encryption.getBytes());
                        out.write(use_compression.getBytes());
                    } else {
                        String socket_name = "[" + name + "]\n";
                        String socket_type = "type = " + suidaoType + "\r\n";
                        String socket_ip = "local_ip = " + local_ip + "\r\n";
                        String socket_port = "local_port =  " + local_port + "\r\n";
                        String socket_subdomain = "subdomain = " + domain_name + "\r\n";
                        String use_encryption = "use_encryption = " + is_secret + "\r\n";
                        String use_compression = "use_compression = " + is_compress + "\r\n";
                        out.write(socket_name.getBytes());
                        out.write(socket_type.getBytes());
                        out.write(socket_ip.getBytes());
                        out.write(socket_port.getBytes());
                        out.write(socket_subdomain.getBytes());
                        out.write(use_encryption.getBytes());
                        out.write(use_compression.getBytes());
                    }

                    break;
                case "udp": {
                    String socket_name = "[" + name + "]\n";
                    String socket_type = "type = " + suidaoType + "\r\n";
                    String socket_ip = "local_ip = " + local_ip + "\r\n";
                    String socket_port = "local_port =  " + local_port + "\r\n";
                    String use_encryption = "use_encryption = " + is_secret + "\r\n";
                    String use_compression = "use_compression = " + is_compress + "\r\n";
                    out.write(socket_name.getBytes());
                    out.write(socket_type.getBytes());
                    out.write(socket_ip.getBytes());
                    out.write(socket_port.getBytes());
                    out.write(use_encryption.getBytes());
                    out.write(use_compression.getBytes());
                    break;
                }
                case "https":
                    if (access_type_rg.getCheckedRadioButtonId() == R.id.custom) {
                        String socket_name = "[" + name + "]\n";
                        String socket_type = "type = " + suidaoType + "\r\n";
                        String socket_ip = "local_ip = " + local_ip + "\r\n";
                        String socket_port = "local_port =  " + local_port + "\r\n";
                        String socket_subdomain = "custom_domains = " + domain_name + "\r\n";
                        String use_encryption = "use_encryption = " + is_secret + "\r\n";
                        String use_compression = "use_compression = " + is_compress + "\r\n";
                        out.write(socket_name.getBytes());
                        out.write(socket_type.getBytes());
                        out.write(socket_ip.getBytes());
                        out.write(socket_port.getBytes());
                        out.write(socket_subdomain.getBytes());
                        out.write(use_encryption.getBytes());
                        out.write(use_compression.getBytes());
                    } else {
                        String socket_name = "[" + name + "]\n";
                        String socket_type = "type = " + suidaoType + "\r\n";
                        String socket_ip = "local_ip = " + local_ip + "\r\n";
                        String socket_port = "local_port =  " + local_port + "\r\n";
                        String socket_subdomain = "subdomain = " + domain_name + "\r\n";
                        String use_encryption = "use_encryption = " + is_secret + "\r\n";
                        String use_compression = "use_compression = " + is_compress + "\r\n";
                        out.write(socket_name.getBytes());
                        out.write(socket_type.getBytes());
                        out.write(socket_ip.getBytes());
                        out.write(socket_port.getBytes());
                        out.write(socket_subdomain.getBytes());
                        out.write(use_encryption.getBytes());
                        out.write(use_compression.getBytes());
                    }
                    break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addsuidao(String user, String ip, String port, String type, String name, String link) {
        SuiDao temp = new SuiDao();
        temp.setUser(user);
        temp.setIp(ip);
        temp.setPort(port);
        temp.setType(type);
        temp.setName(name);
        temp.setLink(link);
        temp.setTime(timeToInvitation());
        DBSuiDaoHelper.insertsuidao(temp);
    }

    public String timeToInvitation() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(date);
    }
}
