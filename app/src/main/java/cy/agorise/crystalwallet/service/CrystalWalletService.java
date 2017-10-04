package cy.agorise.crystalwallet.service;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import cy.agorise.crystalwallet.manager.BitsharesAccountManager;

/**
 * Created by Henry Varona on 3/10/2017.
 */


public class CrystalWalletService extends Service {

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private BitsharesAccountManager  bitsharesAccountManager;
    private Thread LoadAccountTransactionsThread;
    private boolean keepLoadingAccountTransactions;

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            stopSelf(msg.arg1);
        }
    }

    public void loadAccountTransactions(){
        this.keepLoadingAccountTransactions = true;

        while(this.keepLoadingAccountTransactions){
            try{
                Log.i("CrystalServiceLATThread","Searching for transactions...");
                Thread.sleep(60000);//Sleep for 1 minutes //TODO Configurable time
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void onCreate() {
        this.bitsharesAccountManager = new BitsharesAccountManager();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (LoadAccountTransactionsThread == null) {
            LoadAccountTransactionsThread = new Thread() {
                public void run() {
                    loadAccountTransactions();
                }
            };
            LoadAccountTransactionsThread.start();
        }

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.i("Crystal Service", "Destroying service");
    }
}