package com.teched.smartread;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import java.io.File;

public class UploadJob extends Job {

    public static final int PRIORITY = 1;
    private String Path;
    private String TeacherPath;
    private String Name;
    private String s1;
    private String s2;
    File file;
    File file2;

    public UploadJob(String Path, String TeacherPath, String Name, String s1, String s2) {
        super(new Params(PRIORITY).requireNetwork().persist());
        this.Path = Path;
        this.TeacherPath = TeacherPath;
        this.Name = Name;
        this.s1 = s1;
        this.s2 = s2;
    }
    @Override
    public void onAdded() {
    }
    @Override
    public void onRun() throws Throwable {
        int id = JsonClass.uploadFile(s1, s2);
        file = new File(Path + "/" + Name + ".pdf");
        file2 = new File(Path + "/" + String.valueOf(id) + ".pdf");
        file.renameTo(file2);
        file = new File(Path + "/" + Name + ".json");
        file2 = new File(Path + "/" + String.valueOf(id) + ".json");
        file.renameTo(file2);
        file = new File(s1);
        file2 = new File(TeacherPath + "/" + String.valueOf(id) + ".pdf");
        file.renameTo(file2);
        file = new File(s2);
        file2 = new File(TeacherPath + "/" + String.valueOf(id) + ".json");
        file.renameTo(file2);

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