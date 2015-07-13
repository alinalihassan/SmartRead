package com.teched.smartread;

import android.content.Context;
import android.widget.EditText;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class UploadJob extends Job {

    public static final int PRIORITY = 1;
    private String Path;
    private String TeacherPath;
    private String Name;
    private String s1;
    private String s2;
    private String Email;
    private JSONObject jsonObject;

    public UploadJob(String Path, String TeacherPath, String Name, String s1, String s2, String Email, JSONObject jsonObject) {
        super(new Params(PRIORITY).requireNetwork().persist());
        this.Path = Path;
        this.TeacherPath = TeacherPath;
        this.Name = Name;
        this.s1 = s1;
        this.s2 = s2;
        this.Email = Email;
        this.jsonObject = jsonObject;
    }
    @Override
    public void onAdded() {
    }
    @Override
    public void onRun() throws Throwable {
        int id = JsonClass.uploadFile(s1, s2, Email);
        JsonClass.getJSON("http://php-smartread.rhcloud.com/add_book_user.php?email=" + Email + "&book=" + String.valueOf(id));
        MainActivity.copyFile(s1, TeacherPath + "/" + String.valueOf(id) + ".pdf");
        File file = new File(TeacherPath + "/" + String.valueOf(id) + ".json");
        if (!file.exists())
            file.createNewFile();
        OutputStream fo = new FileOutputStream(file, false);
        fo.write(jsonObject.toString().getBytes());
        fo.close();
        MainActivity.copyFile(s1, Path + "/" + String.valueOf(id) + ".pdf");
        MainActivity.copyFile(file.getPath(), Path + "/" +String.valueOf(id) + ".json");
        file = new File(s2);
        file.delete();
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