package com.weisico.sqljdbc;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {
    private View btnTest;
    private View btnClean;
    private TextView tvTestResult;
    private View btnGet;
    private EditText tvCmd;
    private View btnUnlock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnTest = findViewById(R.id.btnTestSql);
        btnClean = findViewById(R.id.btnClean);
        tvTestResult = (TextView) findViewById(R.id.tvTestResult);
        btnUnlock=findViewById(R.id.btnUnlock);

        tvTestResult.setMovementMethod(new ScrollingMovementMethod());

        btnGet=findViewById(R.id.btnGet);
        tvCmd=(EditText)findViewById(R.id.tvCmd);

        btnTest.setOnClickListener(getClickEvent());
        btnClean.setOnClickListener(getClickEvent());
        //btnGet.setOnClickListener(getCmd());
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

    }

    private View.OnClickListener getClickEvent() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTestResult.setText("...");
                if (v == btnTest) {
                    test();

                }
            }
        };
    }
    //查看 进程
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
    private void DEMO() {
        final Integer blk;
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
    private void Unlock() {

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
