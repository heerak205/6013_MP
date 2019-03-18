package com.example.android.background.sync;

import android.app.IntentService;
import android.content.Intent;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
// done (9) Create WaterReminderIntentService and extend it from IntentService
public class WaterReminderIntentService extends IntentService {
    // done (10) Create a default constructor that calls super with the name of this class
    public WaterReminderIntentService() {
        super("WaterReminderIntentService");
    }
    // done (11) Override onHandleIntent
    @Override
    protected void onHandleIntent(Intent intent) {
        // done (12) Get the action from the Intent that started this Service
        String action = intent.getAction();
        // done (13) Call ReminderTasks.executeTask and pass in the action to be performed
        ReminderTasks.executeTask(this, action);
    }
}
