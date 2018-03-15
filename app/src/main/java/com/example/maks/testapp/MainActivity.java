package com.example.maks.testapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView counterTV;
    private TextView lastStartTimeTV;
    private PrefsHelper prefsHelper;
    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefsHelper = new PrefsHelper(this);
        initViews();
        initReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        stopService(new Intent(this, CounterService.class));
    }

    private void initReceiver() {
        receiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(MyBroadcastReceiver.ACTION_COUNTER_UPDATE);
        intentFilter.addAction(MyBroadcastReceiver.ACTION_START_TIME_UPDATE);
        registerReceiver(receiver, intentFilter);
    }

    private void initViews() {
        counterTV = findViewById(R.id.counterStateTV);
        lastStartTimeTV = findViewById(R.id.lastStartTimeTV);

        counterTV.setText(getString(R.string.count_state, prefsHelper.getCounter()));
        lastStartTimeTV.setText(getLastStartTimeString(prefsHelper.getLastStartTime()));
        Button startServiceBtn = findViewById(R.id.startServiceBtn);
        Button stopServiceBtn = findViewById(R.id.stopServiceBtn);

        startServiceBtn.setOnClickListener(v -> {
            startService(new Intent(this, CounterService.class));
        });

        stopServiceBtn.setOnClickListener(v -> {
            stopService(new Intent(this, CounterService.class));
        });
    }

    private String getLastStartTimeString(long lastStartTime) {
        if (lastStartTime == 0) {
            return getString(R.string.service_not_runned);
        }

        DateFormat format = new SimpleDateFormat("dd.MM.yy hh:mm", Locale.getDefault());
        return format.format(new Date(lastStartTime));
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {
        public static final String ACTION_COUNTER_UPDATE = "com.example.maks.action_counter_update";
        public static final String ACTION_START_TIME_UPDATE = "com.example.maks.action_start_time_update";

        @Override
        public void onReceive(final Context context, final Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_COUNTER_UPDATE)) {
                counterTV.setText(getString(R.string.count_state, prefsHelper.getCounter()));
            }
            if (action.equals(ACTION_START_TIME_UPDATE)) {
                lastStartTimeTV.setText(getLastStartTimeString(prefsHelper.getLastStartTime()));
            }
        }
    }

}
