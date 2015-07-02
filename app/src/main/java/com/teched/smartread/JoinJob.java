package com.teched.smartread;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

public class JoinJob extends Job {
    public static final int PRIORITY = 1;
    private String Email;
    private String access_code;

    public JoinJob(String Email, String access_code) {
        super(new Params(PRIORITY).requireNetwork().persist());
        this.Email = Email;
        this.access_code = access_code;
    }
    @Override
    public void onAdded() {
    }
    @Override
    public void onRun() throws Throwable {
        JsonClass.getJSON("http://php-smartread.rhcloud.com/add_class_user.php?email=" + Email + "&access_code=" + access_code);
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
