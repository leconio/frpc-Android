package com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.activity.greendao.DBSuiDaoHelper;
import com.activity.greendao.SuiDao;
import com.frpc.R;
import com.goyourfly.multiple.adapter.MultipleAdapter;
import com.goyourfly.multiple.adapter.MultipleSelect;
import com.goyourfly.multiple.adapter.StateChangeListener;
import com.goyourfly.multiple.adapter.menu.SimpleDeleteSelectAllMenuBar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class KnifeNetListActivity extends AppCompatActivity {

    private UserLoginTask mAuthTask = null;

    private MultipleAdapter adapter;
    private NetAdapter netAdapter;

    private TextView selectTextView;
    private TextView startTextView;
    private int selectPosition = -1;

    private List<SuiDao> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((R.layout.activity_my_suidao));
        assignViews();
        loadData();
        initEvent();
    }

    private void loadData() {
        new Thread() {
            @Override
            public void run() {
                mList.clear();
                mList.addAll(DBSuiDaoHelper.queryAll());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        netAdapter.setData(mList);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }.start();
    }

    @Override
    public void onBackPressed() {
        if (!adapter.cancel(true)) {
            super.onBackPressed();
        }
    }

    private void assignViews() {
        netAdapter = new NetAdapter();
        adapter = MultipleSelect
                .with(this)
                .adapter(netAdapter)
                .linkList(mList)
                .stateChangeListener(listener)
                .customMenu(new SimpleDeleteSelectAllMenuBar(this, getResources().getColor(R.color.colorPrimary), Gravity.TOP))
                .build();
        RecyclerView listView = (RecyclerView) findViewById(R.id.listview);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(adapter);
        selectTextView = (TextView) findViewById(R.id.select_text);
        startTextView = (TextView) findViewById(R.id.start_text);
    }

    private void initEvent() {
        startTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectPosition != -1) {
                    SuiDao suiDao = mList.get(selectPosition);
                    frpStart(suiDao.getName());
                }
            }
        });
        netAdapter.setClickEvent(new NetAdapter.ClickEventListener() {
            @Override
            public void onClick(int position) {
                selectPosition = position;
                selectTextView.setText("已选择：" + mList.get(position).getName());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_item:
                Intent intent = new Intent(KnifeNetListActivity.this, KnifeNetActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    private StateChangeListener listener = new StateChangeListener() {
        @Override
        public void onSelectMode() {

        }

        @Override
        public void onSelect(int i, int i1) {
            selectPosition = i;
            selectTextView.setText("已选择：" + mList.get(i).getName());
        }

        @Override
        public void onUnSelect(int i, int i1) {

        }

        @Override
        public void onDone(@NotNull ArrayList<Integer> arrayList) {

        }

        @Override
        public void onDelete(@NotNull final ArrayList<Integer> arrayList) {
            selectTextView.setText("");
            new Thread() {
                @Override
                public void run() {
                    List<SuiDao> suiDaos = DBSuiDaoHelper.queryAll();
                    boolean[] originPos = new boolean[suiDaos.size()];
                    for (int i = 0; i < suiDaos.size(); i++) {
                        for (SuiDao suiDao1 : mList) {
                            if (suiDaos.get(i).getId().equals(suiDao1.getId())) {
                                originPos[i] = true;
                            }
                        }
                    }

                    for (int i = 0; i < originPos.length; i++) {
                        if (!originPos[i]) {
                            DBSuiDaoHelper.deletesuidao(suiDaos.get(i).getId());
                        }
                    }

                }
            }.start();
        }

        @Override
        public void onCancel() {

        }
    };


    private void frpStart(String name) {
        if (mAuthTask != null) {
            return;
        }
        String path = getExternalFilesDir(null) + "/" + name + "-config.ini";
        mAuthTask = new UserLoginTask(path);
        Log.e("path", path);//打印
        mAuthTask.execute((Void) null);//执行异步线程
        startTextView.setText("已启动");
    }
}
