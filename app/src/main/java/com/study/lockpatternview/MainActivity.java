package com.study.lockpatternview;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

    LockPatterView lock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lock = (LockPatterView) findViewById(R.id.lock_view);
    }
}
