package com.example.yls.wuziqi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private WuZiQiPanel mPanel;
    private Button mRestart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPanel= (WuZiQiPanel) findViewById(R.id.id_panel);
        mRestart= (Button) findViewById(R.id.id_restart);
        mPanel.setListener(new ResultListener() {
            @Override
            public void showResult(int result) {
                String text=(result==WuZiQiPanel.DRAW)?("和棋!"):(result==WuZiQiPanel.WHITE_WON?"白棋获胜!":"黑棋获胜!");
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setIcon(android.R.drawable.ic_dialog_info);
                builder.setTitle("比赛结果:");
                builder.setMessage(text);
                builder.setNegativeButton("再来一局", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPanel.restart();
                    }
                });
                builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.show();
            }
        });
        mRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPanel.restart();
            }
        });
    }
}