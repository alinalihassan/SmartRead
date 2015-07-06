package com.teched.smartread;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

public class AddBookJob extends Job {
    public static final int PRIORITY = 1;
    private String Email;
    private String Book;

    public AddBookJob(String Email, String Book) {
        super(new Params(PRIORITY).requireNetwork().persist());
        this.Email = Email;
        this.Book = Book;
    }
    @Override
    public void onAdded() {
    }
    @Override
    public void onRun() throws Throwable {
        JsonClass.getJSON("http://php-smartread.rhcloud.com/add_book_user.php?email=" + Email + "&book=" + Book);
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
