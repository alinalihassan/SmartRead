package com.teched.smartread;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

public class AddJob extends Job {
    public static final int PRIORITY = 1;
    private String Email;
    private String classId;

    public AddJob(String Email, String classId) {
        super(new Params(PRIORITY).requireNetwork().persist());
        this.Email = Email;
        this.classId = classId;
    }
    @Override
    public void onAdded() {
    }
    @Override
    public void onRun() throws Throwable {
        JsonClass.getJSON("http://php-smartread.rhcloud.com/add_class_user_instant.php?email=" + Email + "&classid=" + classId);
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
