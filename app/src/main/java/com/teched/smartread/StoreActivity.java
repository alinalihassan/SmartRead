package com.teched.smartread;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arasthel.asyncjob.AsyncJob;

import org.json.JSONArray;
import org.json.JSONObject;


public class StoreActivity extends AppCompatActivity {

    private TextView mStoreTitle;
    private TextView mStoreAuthor;
    private TextView mStoreDescription;
    private Button mStoreButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        final Activity this2 = this;
        final boolean[] gotBook = {false};
        final String[] billingBookTitle = new String[1];
        final String[] billingBookAuthor = new String[1];
        final SharedPreferences prefs = this.getSharedPreferences("com.teched.smartread", Context.MODE_PRIVATE);
        mStoreTitle = (TextView) findViewById(R.id.storeTitle);
        mStoreAuthor = (TextView) findViewById(R.id.storeAuthor);
        mStoreDescription = (TextView) findViewById(R.id.storeDescription);
        mStoreButton = (Button) findViewById(R.id.storeButton);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.storeProgress);
        AsyncJob.doInBackground(new AsyncJob.OnBackgroundJob() {
            @Override
            public void doOnBackground() {
                try {
                    JSONObject Book = new JSONObject(JsonClass.getJSON("http://php-smartread.rhcloud.com/get_book_details.php?id=" + MainActivity.billingBookID));
                    JSONArray booksArray = new JSONObject(JsonClass.getJSON("http://php-smartread.rhcloud.com/get_books.php?email=" + prefs.getString("Email", getString(R.string.profile_description)))).getJSONArray("books");
                    for (int i = 0; i < booksArray.length(); i++) {
                        if (booksArray.getString(i).equals(MainActivity.billingBookID)) {
                            gotBook[0] = true;
                            break;
                        }
                    }
                    billingBookTitle[0] = Book.getString("title");
                    billingBookAuthor[0] = Book.getString("author");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                AsyncJob.doOnMainThread(new AsyncJob.OnMainThreadJob() {
                    @Override
                    public void doInUIThread() {
                        progressBar.animate().alpha(0f).setDuration(300).setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                progressBar.setVisibility(View.GONE);
                                mStoreTitle.setText(billingBookTitle[0]);
                                mStoreAuthor.setText(billingBookAuthor[0]);
                                mStoreDescription.setText(MainActivity.bp.getPurchaseListingDetails(MainActivity.billingBookID).description);
                                mStoreButton.setText(MainActivity.bp.getPurchaseListingDetails(MainActivity.billingBookID).priceText);
                                mStoreButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (!isOnline())
                                            Snackbar.make(findViewById(R.id.storeLayout), getResources().getString(R.string.no_connection), Snackbar.LENGTH_SHORT).show();
                                        else if (gotBook[0])
                                            Snackbar.make(findViewById(R.id.storeLayout), "You already got this book", Snackbar.LENGTH_SHORT).show();
                                        else {
                                            MainActivity.bp.purchase(this2, MainActivity.billingBookID);
                                        }
                                    }
                                });
                                mStoreButton.animate().alpha(1).setDuration(300).start();
                                mStoreTitle.animate().alpha(1).setDuration(300).start();
                                mStoreAuthor.animate().alpha(1).setDuration(300).start();
                                mStoreDescription.animate().alpha(1).setDuration(300).start();
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {
                            }
                        }).start();
                    }
                });
            }
        });
    }
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
}