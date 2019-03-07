package com.activity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.frpc.R;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;

public class SGSActivity extends AppCompatActivity {

    private EditText editView;
    private TextView tipsView;
    private Button btnStartView;
    private Button btnRestartView;
    private UserLoginTask mAuthTask;
    private TextView tipTextView;
    private TextView fxgTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sgs);
        assignViews();
        initEvent();
    }


    private void initEvent() {
        editView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                tipsView.setText("请在浏览器输入：http://" + text + ".f.lecon.io");
            }
        });
        btnStartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(editView.getText())) {
                    Toast.makeText(SGSActivity.this, "请完善信息", Toast.LENGTH_SHORT).show();
                } else {
                    frpStart();
                }
            }
        });
        btnRestartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SGSActivity.this, SplashActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                SGSActivity.this.startActivity(intent);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
    }

    private void frpStart() {
        if (mAuthTask != null) {
            return;
        }
        cpToSD();
        String path = getExternalFilesDir(null) + "/config.ini";
        mAuthTask = new UserLoginTask(path);
        Log.e("path", path);//打印
        mAuthTask.execute((Void) null);//执行异步线程
        btnStartView.setEnabled(false);
        editView.setEnabled(false);
        tipTextView.setVisibility(View.VISIBLE);
    }

    private void cpToSD() {
        try {
            InputStream is = getAssets().open("config.ini");
            StringBuilder sb = new StringBuilder();
            String line;

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\r\n");
            }
            String str = sb.toString().replace("{name}", editView.getText().toString());
            String path = getExternalFilesDir(null) + "/config.ini";
            FileWriter writer = new FileWriter(path);
            writer.write(str);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void assignViews() {
        editView = (EditText) findViewById(R.id.edit_emp);
        tipsView = (TextView) findViewById(R.id.text_tips);
        btnStartView = (Button) findViewById(R.id.btn_start);
        btnRestartView = (Button) findViewById(R.id.btn_restart);
        tipTextView = (TextView) findViewById(R.id.tip_text);
        fxgTextView = (TextView) findViewById(R.id.fxg_text);
    }


}
