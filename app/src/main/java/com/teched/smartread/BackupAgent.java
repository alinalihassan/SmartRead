package com.teched.smartread;

import android.app.backup.BackupAgentHelper;
import android.app.backup.SharedPreferencesBackupHelper;

public class BackupAgent extends BackupAgentHelper {

    @Override
    public void onCreate() {
        SharedPreferencesBackupHelper save = new SharedPreferencesBackupHelper(this,"com.teched.smartread");
        addHelper("SharedPreferences",save);
    }
}
