package com.teched.smartread;

import android.app.backup.BackupManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class SettingsActivity extends PreferenceActivity {

    SharedPreferences prefs;
    SharedPreferences preferences;
    SharedPreferences.OnSharedPreferenceChangeListener listener;
    String Path;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Settings");
        addPreferencesFromResource(R.xml.prefs);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        PackageManager m = getPackageManager();
        String s = getPackageName();
        try {
            PackageInfo p = m.getPackageInfo(s, 0);
            s = p.applicationInfo.dataDir+"/app_book";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Path = s;
        Preference feedback = findPreference("send_feedback");
        Preference reset = findPreference("reset");
        Preference privacy = findPreference("privacy_policy");
        findPreference("pref_version").setSummary(BuildConfig.VERSION_NAME);
        preferences = this.getSharedPreferences("com.teched.smartread", Context.MODE_PRIVATE);

        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                switch (key) {
                    case "pref_minimap":
                    case "pref_keepon":
                    case "pref_connection":
                        preferences.edit().putBoolean(key, prefs.getBoolean(key, false)).apply();
                        break;
                    case "pref_frequency": {
                        ListPreference listPreference = (ListPreference) findPreference(key);
                        preferences.edit().putInt("Day", preferences.getInt("Day", 0) + Integer.valueOf(listPreference.getValue()) - preferences.getInt("pref_frequency", 2)).apply();
                        preferences.edit().putInt(key, Integer.valueOf(listPreference.getValue())).apply();
                        break;
                    }
                    case "pref_sort": {
                        ListPreference listPreference = (ListPreference) findPreference(key);
                        preferences.edit().putInt(key, Integer.valueOf(listPreference.getValue())).apply();
                        ((MainAdapter) MainActivity.listView.getAdapter()).flushFilter();
                        break;
                    }
                }
                Backup();
            }
        };
        prefs.registerOnSharedPreferenceChangeListener(listener);
        feedback.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                String uriText =
                        "mailto:superalin40@gmail.com" +
                                "?subject=" + Uri.encode("SmartRead Feedback") +
                                "&body=" + Uri.encode("");

                Uri uri = Uri.parse(uriText);
                Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
                sendIntent.setData(uri);
                startActivity(Intent.createChooser(sendIntent, "Send email"));
                return true;
            }
        });
        reset.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Reset();
                return true;
            }
        });
        privacy.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                String url = "http://www.google.com";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                try {
                    startActivity(i);
                } catch (Exception ignored) {}
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        prefs.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        prefs.unregisterOnSharedPreferenceChangeListener(listener);
    }

    public void Backup() {
        BackupManager bm = new BackupManager(this);
        bm.dataChanged();
    }
    public static String readFromFile(String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        BufferedReader in;
        try {
            in = new BufferedReader(new FileReader(fileName));
            while ((line = in.readLine()) != null) stringBuilder.append(line);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
    public void Write(String Name,JSONObject jsonObject) {
        FileWriter file;
        try {
            file = new FileWriter(Name);
            file.write(jsonObject.toString());
            file.flush();
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Reset() {
        new AlertDialog.Builder(this)
            .setTitle("Reset")
            .setMessage("Are you sure you want to reset all your progress?")
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    File folder = new File(Path);
                    File[] files = folder.listFiles();
                    if (files != null) {
                        for (File file : files) {
                            if (file.getName().endsWith(".json")) {
                                try {
                                    JSONObject jsonObject = new JSONObject(readFromFile(file.getPath()));
                                    jsonObject.put("Finished", false);
                                    for (int i = 0; i < jsonObject.getInt("LastPage"); i++) {
                                        if (!jsonObject.isNull(String.valueOf(i))) {
                                            jsonObject.getJSONArray(String.valueOf(i)).put(0, false);
                                        }
                                    }
                                    jsonObject.put("LastPage", 0);
                                    Write(file.getPath(), jsonObject);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            })
            .show();
    }
}
