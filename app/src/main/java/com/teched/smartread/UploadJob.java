package com.teched.smartread;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

public class UploadJob extends Job {

    public static final int PRIORITY = 1;
    private String s1;
    private String s2;

    public UploadJob(String s1, String s2) {
        super(new Params(PRIORITY).requireNetwork().persist());
        this.s1 = s1;
        this.s2 = s2;
    }
    @Override
    public void onAdded() {
    }
    @Override
    public void onRun() throws Throwable {
        JsonClass.uploadFile(s1, s2);
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