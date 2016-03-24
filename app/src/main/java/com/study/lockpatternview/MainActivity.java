package com.study.lockpatternview;

import com.study.lockpatternview.LockPatterView.OnPatterChangeLister;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnPatterChangeLister {

    LockPatterView lock;
    TextView text;
    String p = "13789";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lock = (LockPatterView) findViewById(R.id.lock_view);
        text = (TextView) findViewById(R.id.text);

        lock.SetOnPatterChangeLister(this);
    }

    @Override
    public void onPatterChange(String passwordStr) {
        if(!TextUtils.isEmpty(passwordStr)) {
            if(passwordStr.equals(p)) {
                text.setText(passwordStr);
            } else {
                text.setText("pass error");
                lock.errorPoint();
            }
        } else {
            Toast.makeText(MainActivity.this, "at least 4 points", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPatterStart(boolean isStart) {
        if(isStart) {
            text.setText("please draw picture");
        }
    }
}
