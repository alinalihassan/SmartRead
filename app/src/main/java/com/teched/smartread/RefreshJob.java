package com.teched.smartread;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RefreshJob extends Job {

    public static final int PRIORITY = 1;
    private String email;
    private File[] files;
    private String path;

    public RefreshJob(String path, String email, File[] files) {
        super(new Params(PRIORITY).requireNetwork().persist());
        this.email = email;
        this.files = files;
        this.path = path;
    }
    @Override
    public void onAdded() {
    }
    @Override
    public void onRun() throws Throwable {
        JSONObject books = new JSONObject(JsonClass.getJSON("http://php-smartread.rhcloud.com/get_books.php?email=" + email));
        JSONArray booksArray = books.getJSONArray("books");
        List<String> files2 = new ArrayList<>();
        if (files != null) {
            for (File file : files)
                files2.add(file.getName());
        }
        for (int i = 0; i < booksArray.length(); i++) {
            if (!files2.contains(booksArray.getString(i) + ".pdf")) {
                JsonClass.DownloadBook(path, booksArray.getString(i) + ".pdf");
                JsonClass.DownloadBook(path, booksArray.getString(i) + ".json");
            }
        }
    }
    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        throwable.printStackTrace();
        return true;
    }
    @Override
    protected void onCancel() {

    }
}