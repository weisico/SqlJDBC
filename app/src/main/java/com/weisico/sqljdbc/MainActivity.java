package com.weisico.sqljdbc;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private View btnTest;
    private View btnClean;
    private TextView tvTestResult;
    private View btnGet;
    private EditText tvCmd;
    private View btnUnlock;
    private ListView sqlList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnTest = findViewById(R.id.btnTestSql);
        btnClean = findViewById(R.id.btnClean);
        tvTestResult = (TextView) findViewById(R.id.tvTestResult);
        btnUnlock=findViewById(R.id.btnUnlock);
        sqlList=(ListView)findViewById(R.id.list_item);
        tvTestResult.setMovementMethod(new ScrollingMovementMethod());

        btnGet=findViewById(R.id.btnGet);
        tvCmd=(EditText)findViewById(R.id.tvCmd);

        //刷新按钮监听命令
        btnTest.setOnClickListener(getClickEvent());
        //清除按钮监听命令
        btnClean.setOnClickListener(getClickEvent());
        //btnGet.setOnClickListener(getCmd());
        //查看按钮监听命令
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = tvCmd.getText().toString();
                if (TextUtils.isEmpty(num)) {
                    Toast.makeText(MainActivity.this, "进程ID不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(MainActivity.this, "提交成功！数字为：" + Double.parseDouble(num), Toast.LENGTH_SHORT).show();
                    tvTestResult.setText("...");
                    if (v == btnGet) {
                        DEMO();
                    }
                }
            }
        });
        //解锁按钮监听命令
        btnUnlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = tvCmd.getText().toString();
                if (TextUtils.isEmpty(num)) {
                    Toast.makeText(MainActivity.this, "进程ID不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(MainActivity.this, "提交成功！数字为：" + Double.parseDouble(num), Toast.LENGTH_SHORT).show();
                    tvTestResult.setText("...");
                    if (v == btnUnlock) {
                        Unlock();
                    }
                }
            }
        });
        //获取点击item信息
        sqlList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                HashMap<String, String> map = (HashMap<String, String>) parent
                        .getItemAtPosition(position);
//                Toast.makeText(view.getContext(), map.get("tv1"),
//                        Toast.LENGTH_SHORT).show();
                if(sqlList.getCount()>0) {
                    tvCmd.setText(map.get("tv1"));
                }
            }
        });

        //
    }
    //刷新按钮 查询进程
    private View.OnClickListener getClickEvent() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTestResult.setText("...");
                if (v == btnTest) {
                   // test();
                    list();

                }
            }
        };
    }
    //查看 进程命令
    private View.OnClickListener getCmd()
    {
//        String num = tvCmd.getText().toString();
//        if (TextUtils.isEmpty(num)) {
//            Toast.makeText(MainActivity.this, "数字不能为空", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(MainActivity.this, "提交成功！数字为：" + Double.parseDouble(num), Toast.LENGTH_SHORT).show();
//        }
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTestResult.setText("...");
                if (v == btnGet) {
                    DEMO();

                }
            }
        };
    }
    //解锁进程
    private View.OnClickListener ToUnlock()
    {
        String s=tvCmd.getText().toString();
        if(TextUtils.isEmpty(s))
        {
            Toast t =Toast.makeText(MainActivity.this, "Please enter item", Toast.LENGTH_SHORT);
            t.show();
        }
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTestResult.setText("...");
                if (v == btnUnlock) {
                    Unlock();

                }
            }
        };
    }
    // 查看进程命令 方法
    private void DEMO() {
        final Integer blk;
        sqlList.setVisibility(View.GONE);
        blk =Integer.parseInt(tvCmd.getText().toString());
        Runnable run = new Runnable() {
            @Override
            public void run() {
                String ret = DBUtil.QueryCmd(blk);
                Message msg = new Message();
                msg.what = 1001;
                Bundle data = new Bundle();
                data.putString("result", ret);
                msg.setData(data);
                mHandler.sendMessage(msg);
            }
        };
        new Thread(run).start();

    }
    //解锁进程方法
    private void Unlock() {
        sqlList.setVisibility(View.VISIBLE);
        final Integer blk;
        blk =Integer.parseInt(tvCmd.getText().toString());
        Runnable run = new Runnable() {
            @Override
            public void run() {
                String ret = DBUtil.QueryUnlock(blk);
                Message msg = new Message();
                msg.what = 1001;
                Bundle data = new Bundle();
                data.putString("result", ret);
                msg.setData(data);
                mHandler.sendMessage(msg);
            }
        };
        new Thread(run).start();

    }
    //刷新获取进程方法
    private void test() {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                String ret = DBUtil.QuerySQL();
                Message msg = new Message();
                msg.what = 1001;
                Bundle data = new Bundle();
                data.putString("result", ret);
                msg.setData(data);
                mHandler.sendMessage(msg);
            }
        };
        new Thread(run).start();

    }
    //参数形式 获取listview 信息··获取数据库数据绑定到listview
    private void list() {
        sqlList.setVisibility(View.VISIBLE);
        Runnable run = new Runnable() {
            @Override
            public void run() {

                ArrayList<Map<String, Object>> ret = DBUtil.show();
                Message message = new Message();
                message.what = 1001;
                message.obj = ret;
                handler2.sendMessage(message);
            }
        };
//        sqlList.getChildAt(0);
//        if(sqlList.getCount()>0) {
//            //            tvCmd.setText(sqlList.);
//            HashMap<String, String> map = (HashMap<String, String>) sqlList
//                    .getItemAtPosition(0);
//            tvCmd.setText(map.get("tv1"));
//
//        }
        new Thread(run).start();


    }
    //接收从子线程发回来的结果进行处理
    Handler  handler2= new Handler(){
        public void handleMessage(android.os.Message msg) {
            if(msg.what == 1001){
                List<Map<String, Object>> data = (List<Map<String, Object>>) msg.obj;
                SimpleAdapter adapter = new SimpleAdapter(MainActivity.this,data,
                        R.layout.moban,
                        new String[]{"tv1","tv2"},
                        new int[]{R.id.tv1,R.id.tv2});
                sqlList.setAdapter(adapter);
//               setListViewHeightBasedOnChildren(sqlList);
            }
        }
    };
    /**
     * 为了解决ListView在ScrollView中只能显示一行数据的问题
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }
    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1001:
                    String str = msg.getData().getString("result");
                    tvTestResult.setText(str);
                    break;

                default:
                    break;
            }
        }

        ;
    };

}
