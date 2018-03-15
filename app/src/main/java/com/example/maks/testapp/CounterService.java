package com.example.maks.testapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class CounterService extends Service {

    public CounterService() {
    }

    private Thread counterThread;
    private PrefsHelper prefsHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        prefsHelper = new PrefsHelper(this);
        prefsHelper.saveLastStartTime(System.currentTimeMillis());
        sendBroadcast(new Intent(MainActivity.MyBroadcastReceiver.ACTION_START_TIME_UPDATE));
        initThread();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!counterThread.isAlive()) {
            counterThread.start();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        counterThread.interrupt();
    }


    private void initThread() {
        counterThread = new Thread(() -> {
            int counter = prefsHelper.getCounter();
            try {
                while (true) {
                    Thread.sleep(5000);
                    counter++;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                prefsHelper.saveCounter(counter);
                sendBroadcast(new Intent(MainActivity.MyBroadcastReceiver.ACTION_COUNTER_UPDATE));
            }
        });
    }

}
