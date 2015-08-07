package com.teched.smartread;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.arasthel.asyncjob.AsyncJob;
import com.balysv.materialmenu.MaterialMenuDrawable;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.heinrichreimersoftware.materialdrawer.DrawerFrameLayout;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerHeaderItem;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerItem;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerProfile;
import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnPageChangeListener;
import com.nononsenseapps.filepicker.FilePickerActivity;
import com.path.android.jobqueue.JobManager;
import com.quinny898.library.persistentsearch.SearchBox;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.yqritc.recyclerviewflexibledivider.FlexibleDividerDecoration;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import net.yanzm.mth.MaterialTabHost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Serializable,BillingProcessor.IBillingHandler {

    public static RecyclerView listView;
    public static String billingBookID;
    public static BillingProcessor bp;
    private static final int FILE_CODE = 1;
    private ActionBarDrawerToggle drawerToggle;
    private String menuString = "Library";
    private String PDFMode = "PDF";
    private RecyclerView.Adapter<MainAdapter.ViewHolder> adapter;
    private DrawerFrameLayout drawer;
    private SlidingUpPanelLayout panelLayout;
    private EditText AuthorEdit;
    private EditText QuestionEdit;
    private CoordinatorLayout teacher;
    private RadioGroup QuestionRadio;
    private SearchBox search;
    private boolean openSearch = false;
    private boolean openAbout = false;
    private boolean openPdf = false;
    private boolean openDistribute = false;
    private File teacherFile;
    private android.support.v7.widget.Toolbar toolbar;
    private ArrayList<Card> list = new ArrayList<>();
    private final ArrayList<File> pdfs = new ArrayList<>();
    private PDFView pdf;
    private JSONObject mainObject = null;
    private JSONObject newObject = null;
    private File cposition;
    private boolean inTransition = false;
    private int correctAnswers = 0;
    public static boolean readyToPurchase = false;
    private transient JobManager jobManager;
    private ArrayList<Class> distributeList;
    private RecyclerView.Adapter<DistributeAdapter.ViewHolder> adapter3;
    private ArrayList<Integer> distribution;
    private ArrayList<String> distributionClasses;
    private String currentBook;
    private ArrayList<Class> classList;
    private RecyclerView.Adapter<ClassAdapter.ViewHolder> adapter4;
    private ArrayList<Users> usersList;
    private RecyclerView.Adapter<UsersAdapter.ViewHolder> adapter5;
    private ViewFlipper viewFlipper;
    private ViewFlipper overviewFlipper;
    private FloatingActionButton classFab;
    private Users lastUser;
    private int lastUserPosition;
    private Users lastUserPending;
    private int lastUserPositionPending;
    private String currentClassId;
    private JSONArray currentClass;
    private JSONArray currentClassPending;
    private NfcAdapter mAdapter;
    private SwipeRefreshLayout refreshLayout;
    private RelativeLayout classes;
    private RelativeLayout store;
    private TextView accessText;
    private SwipeRefreshLayout refreshClasses;
    private SwipeRefreshLayout refreshBooks;
    private ArrayList<Book> booksList;
    private RecyclerView.Adapter<BookAdapter.ViewHolder> adapter6;
    private RecyclerView.Adapter<TeacherAdapter.ViewHolder> adapter7;
    private RecyclerView.Adapter<ClassAdapter.ViewHolder> adapter8;
    private String Path2;
    private String Path3;
    private String Path4;
    private RelativeLayout overview;
    private SwipeRefreshLayout refreshOverview;
    private ArrayList<TeacherBook> overviewList;
    private RecyclerView.Adapter<TeacherAdapter.ViewHolder> adapter2;
    private SwipeRefreshLayout refreshTeacher;
    private ArrayList<TeacherBook> teacherArray;
    private JSONObject bookObject;
    private TextInputLayout titleInput;
    private TextInputLayout authorInput;
    private TextInputLayout questionInput;
    private TextInputLayout pageInput;
    private RecyclerView mStoreRecyclerView;
    private boolean checkPressed = false;
    private ArrayList<Class> overviewClassesList;
    private StatisticsAdapter adapter9;
    private ArrayList<Users> topList;
    private QuestionAdapter adapter10;
    private ArrayList<Question> questionsList;
    private String currentOverviewBook;
    private String currentOverviewClass;
    private Question[][] questionTable;
    private int maxPage;
    private int maxQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        firstTime();
        lockOrientation();
        bp = new BillingProcessor(this, getResources().getString(R.string.license_key), this);
        jobManager = new JobManager(this);
        final SharedPreferences prefs = this.getSharedPreferences("com.teched.smartread", Context.MODE_PRIVATE);
        GregorianCalendar c = new GregorianCalendar();
        c.getTime();
        if(prefs.getInt("Day",0)!=0) {
            if(c.get(Calendar.DAY_OF_YEAR)>=prefs.getInt("Day",0) || c.get(Calendar.YEAR) > prefs.getInt("Year",0))
                try {
                    URL profileURL = new URL(prefs.getString("ProfilePicture", ""));
                    URL coverURL = new URL(prefs.getString("ProfileCover", ""));
                    saveToInternalStorage(this, BitmapFactory.decodeStream(profileURL.openConnection().getInputStream()), "profile");
                    saveToInternalStorage(this, drawableToBitmap(new BitmapDrawable(getResources(), BitmapFactory.decodeStream(coverURL.openConnection().getInputStream()))), "cover");
                    prefs.edit().putInt("Day",c.get(Calendar.DAY_OF_YEAR)+prefs.getInt("perf_frequency",2)).putInt("Year",c.get(Calendar.YEAR)).apply();
                } catch (Exception e) { e.printStackTrace();}
        }
        else {
            prefs.edit().putInt("Day",c.get(Calendar.DAY_OF_YEAR)+prefs.getInt("perf_frequency",2)).apply();
            prefs.edit().putInt("Year", c.get(Calendar.YEAR)).apply();
        }
        PackageManager m = getPackageManager();
        mAdapter = NfcAdapter.getDefaultAdapter(this);
        String s = getPackageName();
        String s2 = "";
        String s3 = "";
        try {
            PackageInfo p = m.getPackageInfo(s, 0);
            s = p.applicationInfo.dataDir+"/app_book";
            s2 = p.applicationInfo.dataDir+"/teacher_book";
            s3 = p.applicationInfo.dataDir+"/tmp";
        } catch (Exception e) {
            e.printStackTrace();
        }
        final String Path = s;
        Path2 = Path;
        Path3 = s2;
        Path4 = s3;
        final String TeacherPath = s2;
        final String TmpPath = s3;
        File[] tmpList = new File(Path4).listFiles();
        if (tmpList != null) {
            for (File file : tmpList)
                file.delete();
        }
        File folder = new File(Path);
        folder.mkdirs();
        new File(TeacherPath).mkdirs();
        new File(TmpPath).mkdirs();
        final MaterialMenuDrawable materialMenu = new MaterialMenuDrawable(this, Color.WHITE, MaterialMenuDrawable.Stroke.THIN);
        drawer = (DrawerFrameLayout) findViewById(R.id.drawer);
        search = (SearchBox) findViewById(R.id.searchbox);
        teacher = (CoordinatorLayout) findViewById(R.id.teacher);
        panelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        pdf = (PDFView) findViewById(R.id.pdfcontent);
        Button nextQuestion = (Button) findViewById(R.id.questionNext);
        ImageView checkButton = (ImageView) findViewById(R.id.check_button);
        AuthorEdit = (EditText) findViewById(R.id.authorEdit);
        QuestionEdit = (EditText) findViewById(R.id.questionEdit);
        QuestionRadio = (RadioGroup) findViewById(R.id.questionGroup);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        overviewFlipper = (ViewFlipper) findViewById(R.id.overviewFlipper);
        final EditText pageEdit = (EditText) findViewById(R.id.pageEdit);
        final EditText answer1 = (EditText) findViewById(R.id.answer1);
        final EditText answer2 = (EditText) findViewById(R.id.answer2);
        final EditText answer3 = (EditText) findViewById(R.id.answer3);
        final EditText answer4 = (EditText) findViewById(R.id.answer4);
        File teacherFolder = new File(TeacherPath);
        final File TList[] = teacherFolder.listFiles();
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        refreshTeacher = (SwipeRefreshLayout) findViewById(R.id.refreshTeacher);
        refreshClasses = (SwipeRefreshLayout) findViewById(R.id.refreshClasses);
        refreshBooks = (SwipeRefreshLayout) findViewById(R.id.refreshStore);
        refreshOverview = (SwipeRefreshLayout) findViewById(R.id.refreshOverview);
        final RecyclerView teacherList = (RecyclerView) findViewById(R.id.teacherList);
        final Button distributeButton = (Button)findViewById(R.id.distributeButton);
        classes = (RelativeLayout) findViewById(R.id.classes);
        store = (RelativeLayout) findViewById(R.id.store);
        overview = (RelativeLayout) findViewById(R.id.overview);
        FloatingActionButton teacherFab = (FloatingActionButton) findViewById(R.id.teacherFab);
        classFab = (FloatingActionButton) findViewById(R.id.classFab);
        FloatingActionButton usersFab = (FloatingActionButton) findViewById(R.id.usersFab);
        accessText = (TextView) findViewById(R.id.access_text);
        TextView programmer = (TextView) findViewById(R.id.aboutProgrammer);
        TextView producer = (TextView) findViewById(R.id.aboutProducer);
        TextView google = (TextView) findViewById(R.id.aboutGoogle);
        TextView facebook = (TextView) findViewById(R.id.aboutFacebook);
        TextView aboutVersion = (TextView) findViewById(R.id.aboutVersion);
        titleInput = (TextInputLayout) findViewById(R.id.pdfTitleLayout);
        authorInput = (TextInputLayout) findViewById(R.id.authorTitle);
        questionInput = (TextInputLayout) findViewById(R.id.questionTitle);
        pageInput = (TextInputLayout) findViewById(R.id.pageTitle);
        setupFloatingLabelError(titleInput,"You must enter a Title!");
        setupFloatingLabelError(authorInput,"You must enter an Author!");
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        MaterialTabHost tabHost = (MaterialTabHost) findViewById(R.id.tabhost);
        tabHost.setType(MaterialTabHost.Type.FullScreenWidth);
        OverviewPagerAdapter pagerAdapter = new OverviewPagerAdapter();
        tabHost.addTab("Top Performers");
        tabHost.addTab("Problematic Questions");
        final ViewPager viewPager2 = (ViewPager) findViewById(R.id.overviewpager);
        viewPager2.setAdapter(pagerAdapter);
        viewPager2.setOnPageChangeListener(tabHost);

        tabHost.setOnTabChangeListener(new MaterialTabHost.OnTabChangeListener() {
            @Override
            public void onTabSelected(int position) {
                viewPager2.setCurrentItem(position);
            }
        });
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
            findViewById(R.id.toolbarCard).setVisibility(View.VISIBLE);
        }
        else {
            toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar2);
            toolbar.setVisibility(View.VISIBLE);
        }
        refreshLayout.setColorSchemeResources(R.color.color_primary);
        refreshTeacher.setColorSchemeResources(R.color.color_primary);
        refreshOverview.setColorSchemeResources(R.color.color_primary);
        refreshClasses.setColorSchemeResources(R.color.color_primary);
        refreshBooks.setColorSchemeResources(R.color.color_primary);
        toolbar.setTitleTextColor(Color.WHITE);
        drawer.setStatusBarBackgroundColor(getResources().getColor(R.color.color_primary_dark));
        aboutVersion.setText("Version " + BuildConfig.VERSION_NAME);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close
        ) {

            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
        };
        drawer.setDrawerListener(drawerToggle);
        drawer.closeDrawer();
        setSupportActionBar(toolbar);
        teacherFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), FilePickerActivity.class);

                i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
                i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
                i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);

                i.putExtra(FilePickerActivity.EXTRA_START_PATH, "/storage/emulated/0/");

                startActivityForResult(i, FILE_CODE);
            }
        });
        classFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createClass();
            }
        });
        usersFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        });
        panelLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float v) {
            }

            @Override
            public void onPanelCollapsed(View view) {
                hideKeyboard();
            }

            @Override
            public void onPanelExpanded(View view) {
            }

            @Override
            public void onPanelAnchored(View view) {

            }

            @Override
            public void onPanelHidden(View view) {

            }
        });

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkPressed && !AuthorEdit.getText().toString().equals("") && !((((EditText) findViewById(R.id.pdfTitle)).getText().toString().equals("")))) {
                    checkPressed = true;
                    AsyncJob.doInBackground(new AsyncJob.OnBackgroundJob() {
                        @Override
                        public void doOnBackground() {
                            try {
                                newObject.put("Author", AuthorEdit.getText().toString());
                                newObject.put("Title", ((EditText) findViewById(R.id.pdfTitle)).getText());
                                File file = new File(TeacherPath + "/" + ((EditText) findViewById(R.id.pdfTitle)).getText() + ".json");
                                if (!file.exists())
                                    file.createNewFile();
                                OutputStream fo = new FileOutputStream(file, false);
                                fo.write(newObject.toString().getBytes());
                                fo.close();
                                int id = JsonClass.uploadFile(teacherFile.getPath(), file.getPath(), prefs.getString("Email", getString(R.string.profile_description)));
                                JsonClass.getJSON("http://php-smartread.rhcloud.com/add_book_user.php?email=" + prefs.getString("Email", getString(R.string.profile_description)) + "&book=" + String.valueOf(id));
                                copyFile(teacherFile.getPath(), TeacherPath + "/" + String.valueOf(id) + ".pdf");
                                File file2 = new File(TeacherPath + "/" + String.valueOf(id) + ".json");
                                if (!file2.exists())
                                    file2.createNewFile();
                                fo = new FileOutputStream(file2, false);
                                fo.write(newObject.toString().getBytes());
                                fo.close();
                                copyFile(teacherFile.getPath(), Path + "/" + String.valueOf(id) + ".pdf");
                                copyFile(file.getPath(), Path + "/" + String.valueOf(id) + ".json");
                                file.delete();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            checkPressed = false;
                        }
                    });
                    hideKeyboard();
                    AnimateTeacher(false);
                    showSnackbar("Your book will be ready for distribution soon", Snackbar.LENGTH_SHORT);
                }
            }
        });
        nextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (QuestionRadio.getCheckedRadioButtonId() != -1 && !QuestionEdit.getText().toString().isEmpty() && (!answer1.getText().toString().isEmpty() || !answer2.getText().toString().isEmpty() || !answer3.getText().toString().isEmpty() || !answer4.getText().toString().isEmpty())) {
                    int page = pageEdit.getText().toString().isEmpty() ? pdf.getCurrentPage()+1 : Integer.parseInt(pageEdit.getText().toString());
                    try {
                        if (newObject.isNull(String.valueOf(page))) {
                            JSONArray arr = new JSONArray();
                            JSONObject object = new JSONObject();
                            JSONArray ans = new JSONArray();
                            arr.put(0, false);
                            arr.put(1, 0);
                            object.put("Question", QuestionEdit.getText().toString());
                            object.put("Trials", 0);
                            ans.put(answer1.getText().toString());
                            ans.put(answer2.getText().toString());
                            ans.put(answer3.getText().toString());
                            ans.put(answer4.getText().toString());
                            object.put("Answers", ans);
                            for (int i = 1; i <= 4; i++) {
                                RadioButton rdio = (RadioButton) findViewById(getResources().getIdentifier("question" + String.valueOf(i), "id", getBaseContext().getPackageName()));
                                if (rdio.isChecked()) {
                                    object.put("Answer", i);
                                    break;
                                }
                            }
                            arr.put(object);
                            newObject.put(String.valueOf(page), arr);
                        } else {
                            JSONObject object = new JSONObject();
                            JSONArray ans = new JSONArray();
                            object.put("Trials", 0);
                            object.put("Question", QuestionEdit.getText().toString());
                            ans.put(answer1.getText().toString());
                            ans.put(answer2.getText().toString());
                            ans.put(answer3.getText().toString());
                            ans.put(answer4.getText().toString());
                            object.put("Answers", ans);
                            for (int i = 1; i <= 4; i++) {
                                RadioButton rdio = (RadioButton) findViewById(getResources().getIdentifier("question" + String.valueOf(i), "id", getBaseContext().getPackageName()));
                                if (rdio.isChecked()) {
                                    object.put("Answer", i);
                                    break;
                                }
                            }
                            newObject.getJSONArray(String.valueOf(page)).put(object);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    QuestionRadio.clearCheck();
                    pageInput.setErrorEnabled(false);
                    questionInput.setErrorEnabled(false);
                    QuestionEdit.setText("");
                    pageEdit.setText("");
                    answer1.setText("");
                    answer2.setText("");
                    answer3.setText("");
                    answer4.setText("");
                }
                else if(QuestionRadio.getCheckedRadioButtonId() == -1) {
                    Snackbar.make(findViewById(R.id.snackLayout), "You must check a correct answer!", Snackbar.LENGTH_SHORT).show();
                }
                else if(QuestionEdit.getText().toString().isEmpty()) {
                    questionInput.setError("You must provide a question!");
                    questionInput.setErrorEnabled(true);
                }
            }
        });
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.facebook_page))));
            }
        });
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.googleplus_page))));
            }
        });
        producer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.dinu_page))));
            }
        });
        programmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.hasssan_page))));
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!openAbout)
                    drawer.openDrawer();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (!openAbout) {
                    if (item.getItemId() == R.id.action_search) {
                        openSearch = true;
                        openSearch();
                    } else if (item.getItemId() == R.id.action_join) {
                        joinClass();
                    }
                }
                return true;
            }
        });
        toolbar.setNavigationIcon(materialMenu);
        refreshTeacher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isOnline()) {
                            final File folder = new File(Path);
                            new AsyncJob.AsyncJobBuilder<Boolean>()
                                    .doInBackground(new AsyncJob.AsyncAction<Boolean>() {
                                        @Override
                                        public Boolean doAsync() {
                                            try {
                                                File teacherFolder = new File(TeacherPath);
                                                File TList[] = teacherFolder.listFiles();
                                                JSONObject books = new JSONObject(JsonClass.getJSON("http://php-smartread.rhcloud.com/get_books.php?email=" + prefs.getString("Email", getString(R.string.profile_description))));
                                                JSONArray booksArray = books.getJSONArray("teacher_books");
                                                List<String> files2 = new ArrayList<>();
                                                if (TList != null) {
                                                    for (File file : TList)
                                                        files2.add(file.getName());
                                                }
                                                for (int i = 0; i < booksArray.length(); i++) {
                                                    if (!files2.contains(booksArray.getString(i) + ".pdf")) {
                                                        JsonClass.DownloadBook(Path, booksArray.getString(i) + ".pdf");
                                                        JsonClass.DownloadBook(Path, booksArray.getString(i) + ".json");
                                                        JsonClass.DownloadBook(TeacherPath, booksArray.getString(i) + ".pdf");
                                                        JsonClass.DownloadBook(TeacherPath, booksArray.getString(i) + ".json");
                                                    }
                                                }
                                                teacherArray.clear();
                                                TList = teacherFolder.listFiles();
                                                if (TList != null) {
                                                    for (File file : TList)
                                                        if (file.getName().endsWith(".pdf")) {
                                                            teacherArray.add(new TeacherBook());
                                                            TeacherBook currentClass = teacherArray.get(teacherArray.size() - 1);
                                                            bookObject = new JSONObject(readFromFile(file.getPath().subSequence(0,file.getPath().length()-4).toString() + ".json"));
                                                            currentClass.name = bookObject.getString("Title");
                                                            currentClass.id = file.getName().replace(".pdf", "");
                                                        }
                                                }
                                                pdfs.clear();
                                                list.clear();
                                                File[] files = folder.listFiles();
                                                if (files != null) {
                                                    for (File file : files)
                                                        if (file.getName().endsWith(".pdf")) {
                                                            pdfs.add(file);
                                                            list.add(new Card());
                                                            list.get(list.size() - 1).name = pdfs.get(pdfs.size() - 1).getName().replace(".pdf", "");
                                                        }
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            return true;
                                        }
                                    })
                                    .doWhenFinished(new AsyncJob.AsyncResultAction<Boolean>() {
                                        @Override
                                        public void onResult(Boolean result) {
                                            ((MainAdapter) listView.getAdapter()).flushFilter();
                                            adapter.notifyDataSetChanged();
                                            adapter2.notifyDataSetChanged();
                                            ((TeacherAdapter)adapter2).flushFilter();
                                            ((MainAdapter)adapter).flushFilter();
                                            toolbar.setTitle("My Books");
                                            refreshTeacher.setRefreshing(false);
                                        }
                                    }).create().start();
                        } else {
                            teacherArray.clear();
                            File teacherFolder = new File(TeacherPath);
                            final File TList[] = teacherFolder.listFiles();
                            if (TList != null) {
                                for (File file : TList)
                                    if (file.getName().endsWith(".pdf")) {
                                        try {
                                            teacherArray.add(new TeacherBook());
                                            TeacherBook currentClass = teacherArray.get(teacherArray.size() - 1);
                                            bookObject = new JSONObject(readFromFile(file.getPath().subSequence(0, file.getPath().length() - 4).toString() + ".json"));
                                            currentClass.name = bookObject.getString("Title");
                                            currentClass.id = file.getName().replace(".pdf", "");
                                        } catch (Exception e) {e.printStackTrace(); }
                                    }
                            }
                            adapter2.notifyDataSetChanged();
                            ((TeacherAdapter)adapter2).flushFilter();
                            toolbar.setTitle("My Books");
                            refreshTeacher.setRefreshing(false);
                        }
                    }
                }, isOnline() ? 250 : 500);
            }
        });
        refreshClasses.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isOnline())
                            refreshClass();
                        else {
                            showSnackbar(getResources().getString(R.string.no_connection), Snackbar.LENGTH_SHORT);
                            refreshClasses.setRefreshing(false);
                        }
                        toolbar.setTitle("My Classes");
                    }
                }, isOnline()?250:500);
            }
        });
        refreshOverview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isOnline())
                            refreshOverview();
                        else {
                            showSnackbar(getResources().getString(R.string.no_connection), Snackbar.LENGTH_SHORT);
                            refreshOverview.setRefreshing(false);
                        }
                    }
                }, isOnline()?250:500);
            }
        });
        refreshBooks.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isOnline())
                            refreshStore();
                        else {
                            showSnackbar(getResources().getString(R.string.no_connection),Snackbar.LENGTH_SHORT);
                            refreshBooks.setRefreshing(false);
                        }
                    }
                }, isOnline()?250:500);
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pdfs.clear();
                        list.clear();
                        final File folder = new File(Path);
                        File[] files = folder.listFiles();
                        if (isOnline()) {
                            new AsyncJob.AsyncJobBuilder<Boolean>()
                                    .doInBackground(new AsyncJob.AsyncAction<Boolean>() {
                                        @Override
                                        public Boolean doAsync() {
                                            try {
                                                File[] files = folder.listFiles();
                                                JSONObject books = new JSONObject(JsonClass.getJSON("http://php-smartread.rhcloud.com/get_books.php?email=" + prefs.getString("Email", getString(R.string.profile_description))));
                                                JSONArray booksArray = books.getJSONArray("books");
                                                List<String> files2 = new ArrayList<>();
                                                if (files != null) {
                                                    for (File file : files)
                                                        files2.add(file.getName());
                                                }
                                                for (int i = 0; i < booksArray.length(); i++) {
                                                    if (!files2.contains(booksArray.getString(i) + ".pdf")) {
                                                        JsonClass.DownloadBook(Path, booksArray.getString(i) + ".pdf");
                                                        JsonClass.DownloadBook(Path, booksArray.getString(i) + ".json");
                                                    }
                                                }
                                                files = folder.listFiles();
                                                if (files != null) {
                                                    for (File file : files)
                                                        if (file.getName().endsWith(".pdf")) {
                                                            pdfs.add(file);
                                                            list.add(new Card());
                                                            list.get(list.size() - 1).name = pdfs.get(pdfs.size() - 1).getName().replace(".pdf", "");
                                                        }
                                                }
                                                if (files != null && isOnline()) {
                                                    for (File file : files) {
                                                        if(file.getName().endsWith(".json")) {
                                                            JsonClass.DownloadJSON(Path4,file.getName(),getApplicationContext().getSharedPreferences("com.teched.smartread", Context.MODE_PRIVATE).getString("Email", getString(R.string.profile_description)));
                                                            JSONObject object = new JSONObject(readFromFile(file.getPath()));
                                                            JSONObject object2 = new JSONObject(readFromFile(Path4 + file.getName()));
                                                            if(object2.getInt("MaxPage")>object.getInt("MaxPage")) {
                                                                String name = file.getName();
                                                                file.delete();
                                                                copyFile(Path4 + name, Path2 + name);
                                                                new File(Path4 + name).delete();
                                                            }
                                                        }
                                                    }
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            return true;
                                        }
                                    })
                                    .doWhenFinished(new AsyncJob.AsyncResultAction<Boolean>() {
                                        @Override
                                        public void onResult(Boolean result) {
                                            toolbar.setTitle("SmartRead");
                                            ((MainAdapter) listView.getAdapter()).flushFilter();
                                            adapter.notifyDataSetChanged();
                                            refreshLayout.setRefreshing(false);
                                        }
                                    }).create().start();
                        }
                        else {
                            if (files != null) {
                                for (File file : files)
                                    if (file.getName().endsWith(".pdf")) {
                                        pdfs.add(file);
                                        list.add(new Card());
                                        list.get(list.size() - 1).name = pdfs.get(pdfs.size() - 1).getName().replace(".pdf", "");
                                    }
                            }
                            toolbar.setTitle("SmartRead");
                            ((MainAdapter) listView.getAdapter()).flushFilter();
                            adapter.notifyDataSetChanged();
                            refreshLayout.setRefreshing(false);
                        }
                    }
                }, isOnline()?250:750);
            }
        });

        final Bitmap[] profile = {null};
        final String profileUrl = prefs.getString("ProfilePicture","");
        Drawable cover = null;
        final String coverUrl = prefs.getString("ProfileCover","");
        try {
            File f=new File(new ContextWrapper(getApplicationContext()).getDir("imageDir", Context.MODE_PRIVATE), "profile.jpg");
            File f2=new File(new ContextWrapper(getApplicationContext()).getDir("imageDir", Context.MODE_PRIVATE), "cover.jpg");
            profile[0] = BitmapFactory.decodeStream(new FileInputStream(f));
            cover = new BitmapDrawable(getResources(), BitmapFactory.decodeStream(new FileInputStream(f2)));
        }
        catch(Exception e) {
            try {
                URL profileURL = new URL(profileUrl);
                URL coverURL = new URL(coverUrl);
                profile[0] = BitmapFactory.decodeStream(profileURL.openConnection().getInputStream());
                cover = new BitmapDrawable(getResources(), BitmapFactory.decodeStream(coverURL.openConnection().getInputStream()));
                saveToInternalStorage(this, profile[0], "profile");
                saveToInternalStorage(this, drawableToBitmap(cover), "cover");
            }
            catch (Exception q) {q.printStackTrace();}
        }
        drawer.addProfile(
                new DrawerProfile()
                        .setRoundedAvatar(this.getBaseContext(), profile[0] == null ? BitmapFactory.decodeResource(getResources(), R.drawable.profile_avatar) : profile[0])
                        .setBackground(cover == null ? getResources().getDrawable(R.drawable.profile_background) : cover)
                        .setName(prefs.getString("ProfileName", getString(R.string.profile_name)))
                        .setDescription(prefs.getString("Email", getString(R.string.profile_description)))
        );
        drawer.addItem(
                new DrawerItem()
                        .setImage(getResources().getDrawable(R.drawable.ic_library), 1)
                        .setTextPrimary("My Library")
                        .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                            @Override
                            public void onClick(DrawerItem drawerItem, long l, int i) {
                                ((MainAdapter) listView.getAdapter()).setType("Library");
                                ((MainAdapter) listView.getAdapter()).flushFilter();
                                if (openPdf && menuString.equals("Library"))
                                    AnimatePDF(false);
                                else if (openPdf && menuString.equals("Teacher"))
                                    AnimateTeacher(false);
                                else if (openDistribute)
                                    AnimateDistribute(false);
                                drawer.closeDrawer();
                                drawer.selectItem(i);
                                menuString = "Library";
                                invalidateOptionsMenu();
                                refreshLayout.setVisibility(View.VISIBLE);
                                teacher.setVisibility(View.INVISIBLE);
                                classes.setVisibility(View.INVISIBLE);
                                store.setVisibility(View.INVISIBLE);
                                overview.setVisibility(View.INVISIBLE);
                                toolbar.setTitle("SmartRead");
                                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
                                    findViewById(R.id.toolbarCard).setElevation(4);
                                    findViewById(R.id.toolbar2).setElevation(4);
                                }
                            }
                        })
        );
        drawer.addItem(
                new DrawerItem()
                        .setImage(getResources().getDrawable(R.drawable.ic_started),1)
                        .setTextPrimary("Started")
                        .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                            @Override
                            public void onClick(DrawerItem drawerItem, long l, int i) {
                                ((MainAdapter) listView.getAdapter()).setType("Started");
                                ((MainAdapter) listView.getAdapter()).flushFilter();
                                if(openPdf && menuString.equals("Library"))
                                    AnimatePDF(false);
                                else if(openPdf && menuString.equals("Teacher"))
                                    AnimateTeacher(false);
                                else if(openDistribute)
                                    AnimateDistribute(false);
                                drawer.closeDrawer();
                                drawer.selectItem(i);
                                menuString = "Library";
                                invalidateOptionsMenu();
                                refreshLayout.setVisibility(View.VISIBLE);
                                teacher.setVisibility(View.INVISIBLE);
                                classes.setVisibility(View.INVISIBLE);
                                store.setVisibility(View.INVISIBLE);
                                overview.setVisibility(View.INVISIBLE);
                                toolbar.setTitle("SmartRead");
                                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
                                    findViewById(R.id.toolbarCard).setElevation(4);
                                    findViewById(R.id.toolbar2).setElevation(4);
                                }
                            }
                        })
        );
        drawer.addItem(
                new DrawerItem()
                        .setImage(getResources().getDrawable(R.drawable.ic_heart_grey), 1)
                        .setTextPrimary("Favorites")
                        .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                            @Override
                            public void onClick(DrawerItem drawerItem, long l, int i) {
                                ((MainAdapter) listView.getAdapter()).setType("Favorites");
                                ((MainAdapter) listView.getAdapter()).flushFilter();
                                if(openPdf && menuString.equals("Library"))
                                    AnimatePDF(false);
                                else if(openPdf && menuString.equals("Teacher"))
                                    AnimateTeacher(false);
                                else if(openDistribute)
                                    AnimateDistribute(false);
                                drawer.closeDrawer();
                                drawer.selectItem(i);
                                menuString = "Library";
                                invalidateOptionsMenu();
                                refreshLayout.setVisibility(View.VISIBLE);
                                teacher.setVisibility(View.INVISIBLE);
                                classes.setVisibility(View.INVISIBLE);
                                store.setVisibility(View.INVISIBLE);
                                overview.setVisibility(View.INVISIBLE);
                                toolbar.setTitle("SmartRead");
                                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
                                    findViewById(R.id.toolbarCard).setElevation(4);
                                    findViewById(R.id.toolbar2).setElevation(4);
                                }
                            }
                        })
        );
        drawer.addItem(
                new DrawerItem()
                        .setImage(getResources().getDrawable(R.drawable.ic_shop), 1)
                        .setTextPrimary("Store")
                        .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                            @Override
                            public void onClick(DrawerItem drawerItem, long l, int i) {
                                if (openPdf && menuString.equals("Library"))
                                    AnimatePDF(false);
                                else if (openPdf && menuString.equals("Teacher"))
                                    AnimateTeacher(false);
                                else if(openDistribute)
                                    AnimateDistribute(false);
                                drawer.closeDrawer();
                                drawer.selectItem(i);
                                menuString = "Store";
                                invalidateOptionsMenu();
                                refreshLayout.setVisibility(View.INVISIBLE);
                                teacher.setVisibility(View.INVISIBLE);
                                classes.setVisibility(View.INVISIBLE);
                                store.setVisibility(View.VISIBLE);
                                overview.setVisibility(View.INVISIBLE);
                                toolbar.setTitle("Store");
                                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
                                    findViewById(R.id.toolbarCard).setElevation(4);
                                    findViewById(R.id.toolbar2).setElevation(4);
                                }
                            }
                        })
        );
        drawer.addItem(new DrawerHeaderItem()
                .setTitle("Teacher")
        );
        drawer.addItem(
                new DrawerItem()
                        .setImage(getResources().getDrawable(R.drawable.ic_school), 1)
                        .setTextPrimary("My Classes")
                        .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                            @Override
                            public void onClick(DrawerItem drawerItem, long l, int i) {
                                if (openPdf && menuString.equals("Library"))
                                    AnimatePDF(false);
                                else if (openPdf && menuString.equals("Teacher"))
                                    AnimateTeacher(false);
                                else if (openDistribute)
                                    AnimateDistribute(false);
                                drawer.closeDrawer();
                                drawer.selectItem(i);
                                menuString = "Class";
                                invalidateOptionsMenu();
                                refreshLayout.setVisibility(View.INVISIBLE);
                                teacher.setVisibility(View.INVISIBLE);
                                classes.setVisibility(View.VISIBLE);
                                store.setVisibility(View.INVISIBLE);
                                overview.setVisibility(View.INVISIBLE);
                                toolbar.setTitle("My Classes");
                                viewFlipper.setInAnimation(getApplicationContext(), R.anim.slide_in_instant);
                                viewFlipper.setOutAnimation(getApplicationContext(), R.anim.slide_out_instant);
                                viewFlipper.setDisplayedChild(0);
                                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
                                    findViewById(R.id.toolbarCard).setElevation(4);
                                    findViewById(R.id.toolbar2).setElevation(4);
                                }
                            }
                        })
        );
        drawer.addItem(
                new DrawerItem()
                        .setImage(getResources().getDrawable(R.drawable.ic_books),1)
                        .setTextPrimary("My Books")
                        .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                            @Override
                            public void onClick(DrawerItem drawerItem, long l, int i) {
                                if(openPdf && menuString.equals("Library"))
                                    AnimatePDF(false);
                                else if(openPdf && menuString.equals("Teacher"))
                                    AnimateTeacher(false);
                                else if(openDistribute)
                                    AnimateDistribute(false);
                                drawer.closeDrawer();
                                drawer.selectItem(i);
                                menuString = "Book";
                                invalidateOptionsMenu();
                                refreshLayout.setVisibility(View.INVISIBLE);
                                teacher.setVisibility(View.VISIBLE);
                                classes.setVisibility(View.INVISIBLE);
                                store.setVisibility(View.INVISIBLE);
                                overview.setVisibility(View.INVISIBLE);
                                toolbar.setTitle("My Books");
                                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
                                    findViewById(R.id.toolbarCard).setElevation(4);
                                    findViewById(R.id.toolbar2).setElevation(4);
                                }
                            }
                        })
        );
        drawer.addItem(
                new DrawerItem()
                        .setImage(getResources().getDrawable(R.drawable.ic_account_multiple), 1)
                        .setTextPrimary("Overview")
                        .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                            @Override
                            public void onClick(DrawerItem drawerItem, long l, int i) {
                                if (openPdf && menuString.equals("Library"))
                                    AnimatePDF(false);
                                else if (openPdf && menuString.equals("Teacher"))
                                    AnimateTeacher(false);
                                else if (openDistribute)
                                    AnimateDistribute(false);
                                drawer.closeDrawer();
                                drawer.selectItem(i);
                                menuString = "";
                                invalidateOptionsMenu();
                                overviewFlipper.setInAnimation(getApplicationContext(), R.anim.slide_in_instant);
                                overviewFlipper.setOutAnimation(getApplicationContext(), R.anim.slide_out_instant);
                                overviewFlipper.setDisplayedChild(0);
                                refreshLayout.setVisibility(View.INVISIBLE);
                                teacher.setVisibility(View.INVISIBLE);
                                classes.setVisibility(View.INVISIBLE);
                                store.setVisibility(View.INVISIBLE);
                                overview.setVisibility(View.VISIBLE);
                                toolbar.setTitle("Overview");
                                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
                                    findViewById(R.id.toolbarCard).setElevation(4);
                                    findViewById(R.id.toolbar2).setElevation(4);
                                }
                            }
                        })
        );

        drawer.addDivider();
        drawer.addItem(
                new DrawerItem()
                        .setImage(getResources().getDrawable(R.drawable.ic_settings), 1)
                        .setTextPrimary("Settings")
                        .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                            @Override
                            public void onClick(DrawerItem drawerItem, long l, int i) {
                                openSettings();
                                drawer.closeDrawer();
                            }
                        })
        );
        drawer.addItem(
                new DrawerItem()
                        .setImage(getResources().getDrawable(R.drawable.ic_information), 1)
                        .setTextPrimary("About")
                        .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                            @Override
                            public void onClick(DrawerItem drawerItem, long l, int i) {
                                if (!openAbout) AnimateAbout(true);
                                drawer.closeDrawer();
                            }
                        })
        );
        refreshJSON();
        drawer.selectItem(0);
        final HorizontalDividerItemDecoration decoration = new HorizontalDividerItemDecoration.Builder(this)
                .paintProvider(new FlexibleDividerDecoration.PaintProvider() {
                    @Override
                    public Paint dividerPaint(int i, RecyclerView recyclerView) {
                        Paint paint = new Paint();
                        paint.setColor(Color.GRAY);
                        paint.setStrokeWidth(1);
                        return paint;
                    }
                })
                .visibilityProvider(new FlexibleDividerDecoration.VisibilityProvider() {
                    @Override
                    public boolean shouldHideDivider(int i, RecyclerView recyclerView) {
                        return getPendingLength() == 0 || i == getPendingLength();
                    }
                })
                .build();


        teacherArray = new ArrayList<>();
        if (TList != null) {
            for (File file : TList)
                if (file.getName().endsWith(".pdf")) {
                    teacherArray.add(new TeacherBook());
                    TeacherBook currentClass = teacherArray.get(teacherArray.size() - 1);
                    try {
                        bookObject = new JSONObject(readFromFile(file.getPath().subSequence(0, file.getPath().length() - 4).toString() + ".json"));
                        currentClass.name = bookObject.getString("Title");
                        currentClass.id = file.getName().replace(".pdf", "");
                    } catch (Exception e) { e.printStackTrace(); }
                }
        }
        RecyclerView.LayoutManager mLayoutManager6 = new LinearLayoutManager(this);
        teacherList.setLayoutManager(mLayoutManager6);
        teacherList.setItemAnimator(new DefaultItemAnimator());
        adapter2 = new TeacherAdapter(teacherArray, R.layout.teacherbookview, this, TeacherPath);
        teacherList.setAdapter(adapter2);
        ((TeacherAdapter) teacherList.getAdapter()).flushFilter();
        teacherList.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), teacherList, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(((TeacherAdapter) teacherList.getAdapter()).getID(position)!=null) {
                    currentBook = ((TeacherAdapter) teacherList.getAdapter()).getID(position);
                    distribute();
                }
                else
                    showSnackbar("Book not ready for distribution", Snackbar.LENGTH_SHORT);
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        }));

        topList = new ArrayList<>();
        final RecyclerView mRecyclerView7 = (RecyclerView) findViewById(R.id.topList);
        RecyclerView.LayoutManager mLayoutManager8 = new LinearLayoutManager(this);
        mRecyclerView7.setLayoutManager(mLayoutManager8);
        mRecyclerView7.setItemAnimator(new DefaultItemAnimator());
        adapter9 = new StatisticsAdapter(topList, R.layout.statisticsview, this);
        mRecyclerView7.setAdapter(adapter9);
        ((StatisticsAdapter) mRecyclerView7.getAdapter()).flushFilter();

        questionsList = new ArrayList<>();
        final RecyclerView mRecyclerView8 = (RecyclerView) findViewById(R.id.questionsList);
        RecyclerView.LayoutManager mLayoutManager9 = new LinearLayoutManager(this);
        mRecyclerView8.setLayoutManager(mLayoutManager9);
        mRecyclerView8.setItemAnimator(new DefaultItemAnimator());
        adapter10 = new QuestionAdapter(questionsList, R.layout.questionview, this);
        mRecyclerView8.setAdapter(adapter10);
        ((QuestionAdapter) mRecyclerView8.getAdapter()).flushFilter();


        overviewClassesList = new ArrayList<>();
        final RecyclerView mRecyclerView6 = (RecyclerView) findViewById(R.id.overviewClassesList);
        RecyclerView.LayoutManager mLayoutManager7 = new LinearLayoutManager(this);
        mRecyclerView6.setLayoutManager(mLayoutManager7);
        mRecyclerView6.setItemAnimator(new DefaultItemAnimator());
        adapter8 = new ClassAdapter(overviewClassesList, R.layout.classview, this);
        mRecyclerView6.setAdapter(adapter8);
        ((ClassAdapter) mRecyclerView6.getAdapter()).flushFilter();
        mRecyclerView6.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), mRecyclerView6, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                overviewFlipper.setVisibility(View.INVISIBLE);
                findViewById(R.id.tabLayout).setVisibility(View.VISIBLE);
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
                    findViewById(R.id.toolbarCard).setElevation(0);
                    findViewById(R.id.toolbar2).setElevation(0);
                }
                currentOverviewClass = ((ClassAdapter)mRecyclerView6.getAdapter()).getId(position);
                refreshStatistics();
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        }));


        overviewList = new ArrayList<>();
        final RecyclerView mRecyclerView5 = (RecyclerView) findViewById(R.id.overviewList);
        RecyclerView.LayoutManager mLayoutManager5 = new LinearLayoutManager(this);
        mRecyclerView5.setLayoutManager(mLayoutManager5);
        mRecyclerView5.setItemAnimator(new DefaultItemAnimator());
        adapter7 = new TeacherAdapter(overviewList, R.layout.teacherbookview, this, TeacherPath);
        mRecyclerView5.setAdapter(adapter7);
        ((TeacherAdapter) mRecyclerView5.getAdapter()).flushFilter();
        refreshOverview();
        mRecyclerView5.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), mRecyclerView5, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                overviewFlipper.setInAnimation(getApplicationContext(), R.anim.slide_in_from_right);
                overviewFlipper.setOutAnimation(getApplicationContext(), R.anim.slide_out_to_left);
                overviewFlipper.showNext();
                currentOverviewBook = ((TeacherAdapter)mRecyclerView5.getAdapter()).getID(position);
                refreshOverviewClasses(currentOverviewBook);
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        }));



        booksList = new ArrayList<>();
        mStoreRecyclerView = (RecyclerView) findViewById(R.id.storeList);
        RecyclerView.LayoutManager mLayoutManager4 = new LinearLayoutManager(this);
        mStoreRecyclerView.setLayoutManager(mLayoutManager4);
        mStoreRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter6 = new BookAdapter(booksList, R.layout.bookview, this);
        mStoreRecyclerView.setAdapter(adapter6);
        ((BookAdapter) mStoreRecyclerView.getAdapter()).flushFilter();
        mStoreRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), mStoreRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemLongClick(View view, int position) {
            }

            @Override
            public void onItemClick(View view, int position) {
                try {
                    if(!isOnline())
                        showSnackbar(getResources().getString(R.string.no_connection), Snackbar.LENGTH_SHORT);
                    else if(!readyToPurchase)
                        showSnackbar("Something went wrong, please try again later", Snackbar.LENGTH_SHORT);
                    else {
                        billingBookID = ((BookAdapter) mStoreRecyclerView.getAdapter()).getID(position);
                        Intent intent = new Intent(getApplicationContext(), StoreActivity.class);
                        startActivity(intent);
                    }

                } catch (Exception e) { e.printStackTrace();}
            }
        }));
        refreshStore();


        usersList = new ArrayList<>();
        final RecyclerView mRecyclerView3 = (RecyclerView) findViewById(R.id.usersList);
        RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(this);
        mRecyclerView3.setLayoutManager(mLayoutManager3);
        mRecyclerView3.setItemAnimator(new DefaultItemAnimator());
        adapter5 = new UsersAdapter(usersList, R.layout.classview, this);
        mRecyclerView3.setAdapter(adapter5);
        mRecyclerView3.addItemDecoration(decoration);

        ((UsersAdapter) mRecyclerView3.getAdapter()).flushFilter();
        final ItemTouchHelper swipeToDismissTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    TextView currentView = ((TextView) viewHolder.itemView.findViewById(R.id.className));
                    if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                        if (viewHolder.getAdapterPosition() >= getPendingLength()) {
                            float ratio = Math.abs(dX) >= 300 ? 1 : Math.abs(dX) / 300;
                            int red = (int) Math.abs((ratio * 244));
                            int green = (int) Math.abs((ratio * 67));
                            int blue = (int) Math.abs((ratio * 54));
                            currentView.setTextColor(Color.rgb(red, green, blue));
                        } else {
                            if (dX < 0) {
                                float ratio = Math.abs(dX) >= 300 ? 1 : Math.abs(dX) / 300;
                                int red = (int) Math.abs((ratio * 244));
                                int green = (int) Math.abs((ratio * 67));
                                int blue = (int) Math.abs((ratio * 54));
                                currentView.setTextColor(Color.rgb(red, green, blue));
                            } else {
                                float ratio = Math.abs(dX) >= 300 ? 1 : Math.abs(dX) / 300;
                                int red = (int) Math.abs((ratio * 76));
                                int green = (int) Math.abs((ratio * 175));
                                int blue = (int) Math.abs((ratio * 80));
                                currentView.setTextColor(Color.rgb(red, green, blue));
                            }
                        }
                    }
            }
            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                if (!isOnline()) {
                    showSnackbar(getResources().getString(R.string.no_connection),Snackbar.LENGTH_SHORT);
                    return 0;
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final RecyclerView.ViewHolder viewHolderf = viewHolder;
                    if (viewHolder.getAdapterPosition() >= currentClassPending.length()) {
                        lastUser = usersList.get(viewHolder.getAdapterPosition());
                        lastUserPosition = viewHolder.getAdapterPosition();
                        usersList.remove(viewHolder.getAdapterPosition());
                        adapter5.notifyItemRemoved(viewHolder.getAdapterPosition());
                        new AsyncJob.AsyncJobBuilder<Boolean>()
                                .doInBackground(new AsyncJob.AsyncAction<Boolean>() {
                                    @Override
                                    public Boolean doAsync() {
                                        JsonClass.getJSON("http://php-smartread.rhcloud.com/remove_class_user.php?id=" + lastUser.id + "&classid=" + currentClassId);
                                        currentClass = RemoveJSONArray(currentClass, lastUser.id);
                                        return true;
                                    }
                                })
                                .doWhenFinished(new AsyncJob.AsyncResultAction<Boolean>() {
                                    @Override
                                    public void onResult(Boolean result) {
                                    }
                                }).create().start();
                        Snackbar.make(findViewById(R.id.classUsersLayout), "User removed", Snackbar.LENGTH_LONG)
                                .setAction(R.string.undo, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        usersList.add(lastUserPosition, lastUser);
                                        TextView currentView = ((TextView) viewHolderf.itemView.findViewById(R.id.className));
                                        currentView.setTextColor(Color.rgb(0, 0, 0));
                                        adapter5.notifyItemInserted(lastUserPosition);
                                        new AsyncJob.AsyncJobBuilder<Boolean>()
                                                .doInBackground(new AsyncJob.AsyncAction<Boolean>() {
                                                    @Override
                                                    public Boolean doAsync() {
                                                        JsonClass.getJSON("http://php-smartread.rhcloud.com/add_class_user_instant.php?id=" + lastUser.id + "&classid=" + currentClassId);
                                                        currentClass.put(lastUser.id);
                                                        return true;
                                                    }
                                                })
                                                .doWhenFinished(new AsyncJob.AsyncResultAction<Boolean>() {
                                                    @Override
                                                    public void onResult(Boolean result) {
                                                    }
                                                }).create().start();
                                    }
                                })
                                .show();
                    } else {
                        lastUserPending = usersList.get(viewHolder.getAdapterPosition());
                        lastUserPositionPending = viewHolder.getAdapterPosition();
                        if (direction == ItemTouchHelper.LEFT) {
                            usersList.remove(viewHolder.getAdapterPosition());
                            adapter5.notifyItemRemoved(viewHolder.getAdapterPosition());
                            new AsyncJob.AsyncJobBuilder<Boolean>()
                                    .doInBackground(new AsyncJob.AsyncAction<Boolean>() {
                                        @Override
                                        public Boolean doAsync() {
                                            try {
                                                JsonClass.getJSON("http://php-smartread.rhcloud.com/remove_class_pending_user.php?id=" + lastUserPending.id + "&classid=" + currentClassId);
                                                currentClassPending = RemoveJSONArray(currentClassPending, lastUserPending.id);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            return true;
                                        }
                                    })
                                    .doWhenFinished(new AsyncJob.AsyncResultAction<Boolean>() {
                                        @Override
                                        public void onResult(Boolean result) {
                                        }
                                    }).create().start();
                            Snackbar.make(findViewById(R.id.classUsersLayout), "Pending user removed", Snackbar.LENGTH_LONG)
                                    .setAction(R.string.undo, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            usersList.add(lastUserPositionPending, lastUserPending);
                                            TextView currentView = ((TextView) viewHolderf.itemView.findViewById(R.id.className));
                                            currentView.setTextColor(Color.rgb(0, 0, 0));
                                            adapter5.notifyItemInserted(lastUserPositionPending);
                                            new AsyncJob.AsyncJobBuilder<Boolean>()
                                                    .doInBackground(new AsyncJob.AsyncAction<Boolean>() {
                                                        @Override
                                                        public Boolean doAsync() {
                                                            JsonClass.getJSON("http://php-smartread.rhcloud.com/add_class_user.php?id=" + lastUserPending.id + "&classid=" + currentClassId);
                                                            currentClassPending.put(lastUserPending.id);
                                                            return true;
                                                        }
                                                    })
                                                    .doWhenFinished(new AsyncJob.AsyncResultAction<Boolean>() {
                                                        @Override
                                                        public void onResult(Boolean result) {
                                                        }
                                                    }).create().start();
                                        }
                                    })
                                    .show();
                        } else {
                            usersList.remove(viewHolder.getAdapterPosition());
                            adapter5.notifyItemRemoved(viewHolder.getAdapterPosition());
                            int pos = usersList.size();
                            usersList.add(pos, lastUserPending);
                            adapter5.notifyItemInserted(pos);
                            ((TextView) viewHolder.itemView.findViewById(R.id.className)).setTextColor(Color.BLACK);
                            new AsyncJob.AsyncJobBuilder<Boolean>()
                                    .doInBackground(new AsyncJob.AsyncAction<Boolean>() {
                                        @Override
                                        public Boolean doAsync() {
                                            JsonClass.getJSON("http://php-smartread.rhcloud.com/remove_class_pending_user.php?id=" + lastUserPending.id + "&classid=" + currentClassId);
                                            JsonClass.getJSON("http://php-smartread.rhcloud.com/add_class_user_instant.php?id=" + lastUserPending.id + "&classid=" + currentClassId);
                                            currentClassPending = RemoveJSONArray(currentClassPending, lastUserPending.id);
                                            currentClass.put(lastUserPending.id);
                                            return true;
                                        }
                                    })
                                    .doWhenFinished(new AsyncJob.AsyncResultAction<Boolean>() {
                                        @Override
                                        public void onResult(Boolean result) {
                                        }
                                    }).create().start();
                        }
                    }
                }
        });
        swipeToDismissTouchHelper.attachToRecyclerView(mRecyclerView3);



        classList = new ArrayList<>();
        final RecyclerView mRecyclerView2 = (RecyclerView) findViewById(R.id.classList);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(this);
        mRecyclerView2.setLayoutManager(mLayoutManager2);
        mRecyclerView2.setItemAnimator(new DefaultItemAnimator());
        adapter4 = new ClassAdapter(classList, R.layout.classview, this);
        mRecyclerView2.setAdapter(adapter4);
        ((ClassAdapter) mRecyclerView2.getAdapter()).flushFilter();
        refreshClass();
        mRecyclerView2.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), mRecyclerView2, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                currentClass = ((ClassAdapter) mRecyclerView2.getAdapter()).getUsers(position);
                currentClassPending = ((ClassAdapter) mRecyclerView2.getAdapter()).getPending(position);
                refreshUsers(currentClassPending, currentClass);
                accessText.setText("Access Code: " + ((ClassAdapter) mRecyclerView2.getAdapter()).getAccessCode(position));
                currentClassId = ((ClassAdapter) mRecyclerView2.getAdapter()).getId(position);
                viewFlipper.setInAnimation(getApplicationContext(), R.anim.slide_in_from_right);
                viewFlipper.setOutAnimation(getApplicationContext(), R.anim.slide_out_to_left);
                menuString = "Users";
                invalidateOptionsMenu();
                viewFlipper.showNext();
                toolbar.setTitle(((ClassAdapter) mRecyclerView2.getAdapter()).getName(position));
            }
            @Override
            public void onItemLongClick(View view, int position) {
            }
        }));

        distribution = new ArrayList<>();
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.distributeList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        distributeList = new ArrayList<>();
        distributionClasses = new ArrayList<>();
        adapter3 = new DistributeAdapter(distributeList, R.layout.distributeview, this);
        mRecyclerView.setAdapter(adapter3);
        ((DistributeAdapter) mRecyclerView.getAdapter()).flushFilter();
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                try {
                    DistributeAdapter.ViewHolder viewHolder = new DistributeAdapter.ViewHolder(view);
                    Class currentClass = distributeList.get(position);
                    if (viewHolder.cardName.isChecked()) {
                        viewHolder.cardName.setChecked(false);
                        for (int i = 0; i < currentClass.users.length(); i++) {
                            Integer r = currentClass.users.getInt(i);
                            distribution.remove(r);
                        }
                        distributionClasses.remove(currentClass.id);
                    }
                    else {
                        viewHolder.cardName.setChecked(true);
                        for (int i = 0; i < currentClass.users.length(); i++) {
                            Integer r = currentClass.users.getInt(i);
                            distribution.add(r);
                        }
                        distributionClasses.add(currentClass.id);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }

        }));
        distributeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOnline()) {
                    new AsyncJob.AsyncJobBuilder<Boolean>()
                            .doInBackground(new AsyncJob.AsyncAction<Boolean>() {
                                @Override
                                public Boolean doAsync() {
                                    distribution = new ArrayList<>(new LinkedHashSet<>(distribution));
                                    for (int i = 0; i < distribution.size(); i++)
                                        JsonClass.getJSON("http://php-smartread.rhcloud.com/add_book_user.php?id=" + String.valueOf(distribution.get(i)) + "&book=" + currentBook);
                                    for (int i = 0; i < distributionClasses.size(); i++)
                                        JsonClass.getJSON("http://php-smartread.rhcloud.com/add_book_class.php?classid=" + distributionClasses.get(i) + "&book=" + currentBook);
                                    return true;
                                }
                            })
                            .doWhenFinished(new AsyncJob.AsyncResultAction<Boolean>() {
                                @Override
                                public void onResult(Boolean result) {

                                }
                            }).create().start();
                    AnimateDistribute(false);
                    showSnackbar("The document will be distributed", Snackbar.LENGTH_SHORT);
                }
                else
                    showSnackbar(getResources().getString(R.string.no_connection), Snackbar.LENGTH_SHORT);
            }
        });


        adapter = new MainAdapter(list,R.layout.cardview, this, Path);
        listView = (RecyclerView)findViewById(R.id.list);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files)
                if (file.getName().endsWith(".pdf")) {
                    pdfs.add(file);
                    list.add(new Card());
                    list.get(list.size()-1).name = pdfs.get(pdfs.size() - 1).getName().replace(".pdf", "");
                }
        }
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setAdapter(adapter);
        ((MainAdapter) listView.getAdapter()).flushFilter();
        listView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), listView, new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemLongClick(View view, int position) {
                try {
                    MainAdapter.ViewHolder viewHolder = new MainAdapter.ViewHolder(view);
                    mainObject = new JSONObject(readFromFile(Path + "/" + ((MainAdapter) listView.getAdapter()).getName(position) + ".json"));
                    if (mainObject.getBoolean("Favorite")) {
                        mainObject.put("Favorite", false);
                        YoYo.with(Techniques.ZoomOut)
                                .duration(250)
                                .interpolate(new AccelerateInterpolator())
                                .playOn(viewHolder.cardHeart);
                        Write(Path + "/" + ((MainAdapter) listView.getAdapter()).getName(position) + ".json",mainObject);
                    }
                    else {
                        mainObject.put("Favorite", true);
                        YoYo.with(Techniques.ZoomIn)
                                .duration(250)
                                .interpolate(new AccelerateInterpolator())
                                .playOn(viewHolder.cardHeart);
                        Write(Path + "/" + ((MainAdapter) listView.getAdapter()).getName(position) + ".json", mainObject);
                    }
                } catch (Exception e) {e.printStackTrace(); }
            }

            @Override
            public void onItemClick(View view, int position) {
                if (!openAbout && !inTransition) {
                    pdf.invalidate();
                    PDFMode = "PDF";
                    File pdfFile = null;
                    try {
                        pdfFile = new File(Path + "/" + ((MainAdapter) listView.getAdapter()).getName(position) + ".pdf");
                    } catch(Exception e) {e.printStackTrace();}
                    cposition = pdfFile;
                    try {
                        mainObject = new JSONObject(readFromFile(Path + "/" + ((MainAdapter) listView.getAdapter()).getName(position) +".json"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    final JSONObject mainObject2 = mainObject;
                    try {
                        if (pdfFile != null) {
                            pdf.fromFile(pdfFile)
                                    .defaultPage(mainObject.getInt("LastPage") != 0 ? mainObject.getInt("LastPage") : 1)
                                    .showMinimap(prefs.getBoolean("pref_minimap", false))
                                    .enableSwipe(true)
                                    .onPageChange(new OnPageChangeListener() {
                                        @Override
                                        public void onPageChanged(int page, int pageCount) {
                                            try {
                                                JSONArray array;
                                                boolean canDo = false;
                                                if(mainObject2.getString("Date").equals("")) {
                                                    SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
                                                    Date date = new Date();
                                                    String datetime = dateformat.format(date);
                                                    mainObject2.put("Date",datetime);
                                                }
                                                if (page == pdf.getPageCount())
                                                    mainObject2.put("Finished", true);
                                                mainObject2.put("LastPage", page);
                                                if(mainObject2.getInt("MaxPage")<page)
                                                    mainObject2.put("MaxPage", page);
                                                array = mainObject2.getJSONArray(String.valueOf(page));
                                                if (array != null) canDo = true;
                                                if (canDo && !array.getBoolean(0)) {
                                                    QuestionPage(array, 2, mainObject2);
                                                    AnimateQuestion(true);
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    })
                                    .load();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    AnimatePDF(panelLayout.getVisibility() != View.VISIBLE);
                }
            }
        }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mAdapter!=null)
            setupForegroundDispatch(this, mAdapter);
        handleIntent(getIntent());
    }

    @Override
    protected void onPause() {
        if(mAdapter!=null)
           stopForegroundDispatch(this, mAdapter);
        super.onPause();
    }
    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            String type = intent.getType();
            if ("text/plain".equals(type)) {

                final Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                TagRead(tag);
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    TagRead(tag);
                    break;
                }
            }
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        switch (menuString) {
            case "Library":
                menu.getItem(0).setVisible(true);
                menu.getItem(1).setVisible(true);
                break;
            case "Store":
                menu.getItem(1).setVisible(true);
                menu.getItem(0).setVisible(false);
                break;
            case "Book":
                menu.getItem(1).setVisible(true);
                menu.getItem(0).setVisible(false);
                break;
            case "Class":
                menu.getItem(1).setVisible(true);
                menu.getItem(0).setVisible(false);
                break;
            default:
                menu.getItem(0).setVisible(false);
                menu.getItem(1).setVisible(false);
                break;
        }
        return true;
    }

    @Override
    public boolean dispatchTouchEvent (@NonNull MotionEvent ev) {
        if((openSearch && ev.getRawY()>=getStatusBarHeight() + toolbar.getHeight() + 10) ){
            openSearch = false;
            search.toggleSearch();
            closeSearch();
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }
    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        final String productIdf = productId;
        showSnackbar("You just purchased " + bp.getPurchaseListingDetails(productId).title, Snackbar.LENGTH_SHORT);
        new AsyncJob.AsyncJobBuilder<Boolean>()
                .doInBackground(new AsyncJob.AsyncAction<Boolean>() {
                    @Override
                    public Boolean doAsync() {
                        JsonClass.getJSON("http://php-smartread.rhcloud.com/add_book_user.php?email=" + getApplicationContext().getSharedPreferences("com.teched.smartread", Context.MODE_PRIVATE).getString("Email", getString(R.string.profile_description)) + "&book=" + productIdf);
                        return true;
                    }
                })
                .doWhenFinished(new AsyncJob.AsyncResultAction<Boolean>() {
                    @Override
                    public void onResult(Boolean result) {
                        bp.consumePurchase(productIdf);
                        pdfs.clear();
                        list.clear();
                        final File folder = new File(Path2);
                        File[] files = folder.listFiles();
                        if (files != null) {
                            for (File file : files)
                                if (file.getName().endsWith(".pdf")) {
                                    pdfs.add(file);
                                    list.add(new Card());
                                    list.get(list.size() - 1).name = pdfs.get(pdfs.size() - 1).getName().replace(".pdf", "");
                                }
                        }
                        adapter.notifyDataSetChanged();
                    }
                }).create().start();
        }

    @Override
    public void onBillingError(int errorCode, Throwable error) {

    }
    @Override
    public void onBillingInitialized() {
        readyToPurchase = true;
    }
    @Override
    public void onPurchaseHistoryRestored() {
        List<String> ownedProducts = bp.listOwnedProducts();
        for(int i = 0;i<ownedProducts.size();i++) {
            final String book = ownedProducts.get(i);
            new AsyncJob.AsyncJobBuilder<Boolean>()
                    .doInBackground(new AsyncJob.AsyncAction<Boolean>() {
                        @Override
                        public Boolean doAsync() {
                            JsonClass.getJSON("http://php-smartread.rhcloud.com/add_book_user.php?email=" + getApplicationContext().getSharedPreferences("com.teched.smartread", Context.MODE_PRIVATE).getString("Email", getString(R.string.profile_description)) + "&book=" + book);
                            return true;
                        }
                    })
                    .doWhenFinished(new AsyncJob.AsyncResultAction<Boolean>() {
                        @Override
                        public void onResult(Boolean result) {
                            bp.consumePurchase(book);
                            pdfs.clear();
                            list.clear();
                            final File folder = new File(Path2);
                            File[] files = folder.listFiles();
                            if (files != null) {
                                for (File file : files)
                                    if (file.getName().endsWith(".pdf")) {
                                        pdfs.add(file);
                                        list.add(new Card());
                                        list.get(list.size() - 1).name = pdfs.get(pdfs.size() - 1).getName().replace(".pdf", "");
                                    }
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }).create().start();
            }
    }

    @Override
    public void onDestroy() {
        if (bp != null)
            bp.release();

        super.onDestroy();
    }

    public void onBackPressed() {
        if(!inTransition) {
            View myView2 = findViewById(R.id.aboutScreen);
            if (myView2.getVisibility() == View.VISIBLE)
                AnimateAbout(false);
            else if (findViewById(R.id.QuestionLayout).getVisibility() != View.VISIBLE) {
                View myView = findViewById(R.id.sliding_layout);
                View myView3 = findViewById(R.id.distributeScreen);
                if(panelLayout.getPanelState()== SlidingUpPanelLayout.PanelState.EXPANDED)
                    panelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                else if (myView.getVisibility() == View.VISIBLE && PDFMode.equals("PDF")) {
                    AnimatePDF(false);
                    new AsyncJob.AsyncJobBuilder<Boolean>()
                            .doInBackground(new AsyncJob.AsyncAction<Boolean>() {
                                @Override
                                public Boolean doAsync() {
                                    JsonClass.uploadJSON(cposition.getPath().replace(".pdf",".json"), getApplicationContext().getSharedPreferences("com.teched.smartread", Context.MODE_PRIVATE).getString("Email", getString(R.string.profile_description)));
                                    return true;
                                }
                            }).create().start();
                }
                else if(myView.getVisibility() == View.VISIBLE && PDFMode.equals("Teacher"))
                    AnimateTeacher(false);
                else if(myView3.getVisibility() == View.VISIBLE)
                    AnimateDistribute(false);
                else if(findViewById(R.id.tabLayout).getVisibility()==View.VISIBLE) {
                    findViewById(R.id.overviewFlipper).setVisibility(View.VISIBLE);
                    findViewById(R.id.tabLayout).setVisibility(View.INVISIBLE);
                }
                else if(viewFlipper.getDisplayedChild()==1 && classes.getVisibility()==View.VISIBLE ) {
                    viewFlipper.setInAnimation(getApplicationContext(), R.anim.slide_in_from_left);
                    viewFlipper.setOutAnimation(getApplicationContext(), R.anim.slide_out_to_right);
                    viewFlipper.showPrevious();
                    menuString = "Class";
                    invalidateOptionsMenu();
                    toolbar.setTitle("My Classes");
                }
                else if(overviewFlipper.getDisplayedChild()==1 && overview.getVisibility()==View.VISIBLE) {
                    overviewFlipper.setInAnimation(getApplicationContext(), R.anim.slide_in_from_left);
                    overviewFlipper.setOutAnimation(getApplicationContext(), R.anim.slide_out_to_right);
                    overviewFlipper.showPrevious();
                }
                else
                    super.onBackPressed();
            }
        }
    }

    public void AnimatePDF(boolean bool) {
        if(inTransition) return;
        inTransition = true;
        final View myView = findViewById(R.id.sliding_layout);
        panelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        YoYo.with(Techniques.ZoomIn)
                .duration(400)
                .interpolate(new AccelerateInterpolator())
                .withListener(new com.nineoldandroids.animation.Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(com.nineoldandroids.animation.Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(com.nineoldandroids.animation.Animator animation) {
                        inTransition = false;
                    }

                    @Override
                    public void onAnimationCancel(com.nineoldandroids.animation.Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(com.nineoldandroids.animation.Animator animation) {
                    }
                })
                .playOn(myView);
        if (bool) {
            myView.setVisibility(View.VISIBLE);
            openPdf = true;
            if(this.getSharedPreferences("com.teched.smartread", Context.MODE_PRIVATE).getBoolean("pref_keepon",false))
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            pdf.enableSwipe(true);
        } else {
            pdf.enableSwipe(false);
            openPdf = false;
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            YoYo.with(Techniques.ZoomOut)
                    .duration(400)
                    .interpolate(new AccelerateInterpolator())
                    .withListener(new com.nineoldandroids.animation.Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(com.nineoldandroids.animation.Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(com.nineoldandroids.animation.Animator animation) {
                            inTransition = false;
                            myView.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(com.nineoldandroids.animation.Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(com.nineoldandroids.animation.Animator animation) {
                        }
                    })
                    .playOn(myView);
        }
        Write(null,null);
    }

    public void AnimateQuestion(boolean bool) {
        final View myView = findViewById(R.id.QuestionLayout);

        YoYo.with(Techniques.ZoomIn)
                .duration(400)
                .interpolate(new AccelerateInterpolator())
                .playOn(myView);
        if (bool) {
            myView.setVisibility(View.VISIBLE);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            View quest = findViewById(R.id.Question);
            quest.setVisibility(View.VISIBLE);
            RadioButton quest1 = (RadioButton)findViewById(R.id.Option1);
            quest1.setTextColor(Color.BLACK);
            quest1.setVisibility(View.VISIBLE);
            quest1 = (RadioButton)findViewById(R.id.Option2);
            quest1.setTextColor(Color.BLACK);
            quest1.setVisibility(View.VISIBLE);
            quest1 = (RadioButton)findViewById(R.id.Option3);
            quest1.setTextColor(Color.BLACK);
            quest1.setVisibility(View.VISIBLE);
            quest1 = (RadioButton)findViewById(R.id.Option4);
            quest1.setTextColor(Color.BLACK);
            quest1.setVisibility(View.VISIBLE);
            quest = findViewById(R.id.button);
            quest.setVisibility(View.VISIBLE);
        } else {
            YoYo.with(Techniques.ZoomOut)
                    .duration(400)
                    .interpolate(new AccelerateInterpolator())
                    .withListener(new com.nineoldandroids.animation.Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(com.nineoldandroids.animation.Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(com.nineoldandroids.animation.Animator animation) {
                            myView.setVisibility(View.INVISIBLE);
                            View myView = findViewById(R.id.QuestionLayout);
                            myView.setVisibility(View.INVISIBLE);
                            View quest = findViewById(R.id.Question);
                            quest.setVisibility(View.INVISIBLE);
                            RadioButton quest1 = (RadioButton)findViewById(R.id.Option1);
                            quest1.setTextColor(Color.BLACK);
                            quest1.setVisibility(View.INVISIBLE);
                            quest1 = (RadioButton)findViewById(R.id.Option2);
                            quest1.setTextColor(Color.BLACK);
                            quest1.setVisibility(View.INVISIBLE);
                            quest1 = (RadioButton)findViewById(R.id.Option3);
                            quest1.setTextColor(Color.BLACK);
                            quest1.setVisibility(View.INVISIBLE);
                            quest1 = (RadioButton)findViewById(R.id.Option4);
                            quest1.setTextColor(Color.BLACK);
                            quest1.setVisibility(View.INVISIBLE);
                            quest = findViewById(R.id.button);
                            quest.setVisibility(View.INVISIBLE);
                            pdf.enableSwipe(true);
                            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                        }

                        @Override
                        public void onAnimationCancel(com.nineoldandroids.animation.Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(com.nineoldandroids.animation.Animator animation) {
                        }
                    })
                    .playOn(myView);
        }
    }

    public void AnimateAbout(boolean bool) {
        inTransition = true;
        final View myView = findViewById(R.id.aboutScreen);
        SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        YoYo.with(Techniques.ZoomIn)
                .duration(400)
                .interpolate(new AccelerateInterpolator())
                .withListener(new com.nineoldandroids.animation.Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(com.nineoldandroids.animation.Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(com.nineoldandroids.animation.Animator animation) {
                        inTransition = false;
                    }

                    @Override
                    public void onAnimationCancel(com.nineoldandroids.animation.Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(com.nineoldandroids.animation.Animator animation) {
                    }
                })
                .playOn(myView);
        if (bool) {
            myView.setVisibility(View.VISIBLE);
            openAbout = true;
            refreshLayout.setEnabled(false);
        } else {
            openAbout = false;
            refreshLayout.setEnabled(true);
            YoYo.with(Techniques.ZoomOut)
                    .duration(400)
                    .interpolate(new AccelerateInterpolator())
                    .withListener(new com.nineoldandroids.animation.Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(com.nineoldandroids.animation.Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(com.nineoldandroids.animation.Animator animation) {
                            inTransition = false;
                            myView.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(com.nineoldandroids.animation.Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(com.nineoldandroids.animation.Animator animation) {
                        }
                    })
                    .playOn(myView);
        }
    }

    public void AnimateTeacher(boolean bool) {
        if(inTransition) return;
        inTransition = true;
        final View myView = findViewById(R.id.sliding_layout);

        YoYo.with(Techniques.ZoomIn)
                .duration(400)
                .interpolate(new AccelerateInterpolator())
                .withListener(new com.nineoldandroids.animation.Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(com.nineoldandroids.animation.Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(com.nineoldandroids.animation.Animator animation) {
                        panelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        inTransition = false;
                    }

                    @Override
                    public void onAnimationCancel(com.nineoldandroids.animation.Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(com.nineoldandroids.animation.Animator animation) {
                    }
                })
                .playOn(myView);
        if (bool) {
            myView.setVisibility(View.VISIBLE);
            openPdf = true;
            if(this.getSharedPreferences("com.teched.smartread", Context.MODE_PRIVATE).getBoolean("pref_keepon",false))
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            pdf.enableSwipe(true);
        } else {
            pdf.enableSwipe(false);
            openPdf = false;
            panelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            YoYo.with(Techniques.ZoomOut)
                    .duration(400)
                    .interpolate(new AccelerateInterpolator())
                    .withListener(new com.nineoldandroids.animation.Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(com.nineoldandroids.animation.Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(com.nineoldandroids.animation.Animator animation) {
                            myView.setVisibility(View.INVISIBLE);
                            final EditText pageEdit = (EditText) findViewById(R.id.pageEdit);
                            final EditText answer1 = (EditText) findViewById(R.id.answer1);
                            final EditText answer2 = (EditText) findViewById(R.id.answer2);
                            final EditText answer3 = (EditText) findViewById(R.id.answer3);
                            final EditText answer4 = (EditText) findViewById(R.id.answer4);
                            AuthorEdit.setText("");
                            QuestionEdit.setText("");
                            QuestionRadio.clearCheck();
                            pageEdit.setText("");
                            answer1.setText("");
                            answer2.setText("");
                            answer3.setText("");
                            answer4.setText("");
                            titleInput.setErrorEnabled(false);
                            authorInput.setErrorEnabled(false);
                            questionInput.setErrorEnabled(false);
                            pageInput.setErrorEnabled(false);
                            inTransition = false;
                        }

                        @Override
                        public void onAnimationCancel(com.nineoldandroids.animation.Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(com.nineoldandroids.animation.Animator animation) {
                        }
                    })
                    .playOn(myView);
        }
    }

    public void AnimateDistribute(boolean bool) {
        if(inTransition) return;
        inTransition = true;
        final View myView = findViewById(R.id.distributeScreen);
        YoYo.with(Techniques.ZoomIn)
                .duration(400)
                .interpolate(new AccelerateInterpolator())
                .withListener(new com.nineoldandroids.animation.Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(com.nineoldandroids.animation.Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(com.nineoldandroids.animation.Animator animation) {
                        inTransition = false;
                    }

                    @Override
                    public void onAnimationCancel(com.nineoldandroids.animation.Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(com.nineoldandroids.animation.Animator animation) {
                    }
                })
                .playOn(myView);
        if (bool) {
            myView.setVisibility(View.VISIBLE);
            openDistribute=true;
        } else {
            YoYo.with(Techniques.ZoomOut)
                    .duration(400)
                    .interpolate(new AccelerateInterpolator())
                    .withListener(new com.nineoldandroids.animation.Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(com.nineoldandroids.animation.Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(com.nineoldandroids.animation.Animator animation) {
                            inTransition = false;
                            openDistribute=false;
                            myView.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(com.nineoldandroids.animation.Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(com.nineoldandroids.animation.Animator animation) {
                        }
                    })
                    .playOn(myView);
        }
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

    public void QuestionPage(final JSONArray array, final int i, final JSONObject main) {
        try {
            final int max = main.getInt("LastPage");
            final JSONObject obj = array.getJSONObject(i);
            JSONArray jsonArray = obj.getJSONArray("Answers");
            pdf.enableSwipe(false);
            final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
            TextView quest = (TextView) findViewById(R.id.Question);
            fade(quest, obj.getString("Question"));
            for(int j = 0;j<jsonArray.length();j++) {
                RadioButton rdio = (RadioButton) findViewById(getResources().getIdentifier("Option" + String.valueOf(j+1), "id", getBaseContext().getPackageName()));
                fade(rdio, jsonArray.getString(j));
            }
            final Button btn = (Button) findViewById(R.id.button);
            if (!array.isNull(i + 1)) fade(btn, "Next");
            else fade(btn, "Finish");
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (radioGroup.getCheckedRadioButtonId() != -1) {
                        try {
                            RadioButton rdio = (RadioButton) findViewById(getResources().getIdentifier("Option" + String.valueOf(obj.getInt("Answer")), "id", getBaseContext().getPackageName()));
                            if (rdio.isChecked()) correctAnswers++;
                            else obj.put("Trials", obj.getInt("Trials") + 1);
                            if (array.isNull(i + 1)) {
                                if(correctAnswers>=array.getInt(1)) {
                                    array.put(0, true);
                                    Write(null, null);
                                    correctAnswers=0;
                                    if(!rdio.isChecked()) WrongAnim(radioGroup);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            AnimateQuestion(false);
                                        }
                                    }, 350);
                                }
                                else {
                                    correctAnswers= 0;
                                    if(!rdio.isChecked()) WrongAnim(radioGroup);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            AnimateQuestion(false);
                                            int page = 1;
                                            for(int j = max-1;j>1;j--) {
                                                if(!main.isNull(String.valueOf(j))) {
                                                    page = j;
                                                    break;
                                                }
                                            }
                                            pdf.jumpTo(page);
                                        }
                                    }, 350);
                                }
                            }
                            else if (rdio.isChecked()) {
                                QuestionPage(array, i + 1, main);
                            }
                            else {
                                WrongAnim(radioGroup);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        QuestionPage(array, i + 1, main);
                                    }
                                }, 350);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        radioGroup.clearCheck();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void WrongAnim(RadioGroup radioGroup) {
        final RadioButton radioButton = (RadioButton)findViewById(radioGroup.getCheckedRadioButtonId());
        Integer colorFrom = Color.BLACK;
        Integer colorTo = Color.RED;
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(250);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                radioButton.setTextColor((Integer) animator.getAnimatedValue());
            }
        });
        colorAnimation.start();
    }

    public void fade(final View view, final String txt) {
        final boolean visible = view.getVisibility() == View.VISIBLE;
        view.animate()
                .alpha(visible ? 0.0f : 1.0f)
                .setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (visible) {
                            try {
                                RadioButton rdio = (RadioButton) view;
                                rdio.setTextColor(Color.BLACK);
                            } catch (Exception ignored) {
                            }
                            view.animate()
                                    .alpha(1.0f)
                                    .setDuration(200)
                                    .setListener(null);
                        }
                        TextView view1 = (TextView) view;
                        if (txt.equals(""))
                            view.setVisibility(View.GONE);
                        else {
                            view.setVisibility(View.VISIBLE);
                            view.setAlpha(0.0f);
                            view.animate()
                                    .alpha(1.0f)
                                    .setDuration(200)
                                    .setListener(null);
                            view1.setText(txt);
                        }
                    }
                });
    }

    public void Write(@Nullable String Name,@Nullable JSONObject jsonObject) {
        FileWriter file;
        try {
            file = new FileWriter(Name == null? cposition.getPath().replace(".pdf", ".json") : Name);
            file.write(jsonObject == null?mainObject.toString(): jsonObject.toString());
            file.flush();
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void firstTime() {
        SharedPreferences prefs = this.getSharedPreferences("com.teched.smartread", Context.MODE_PRIVATE);
        if(prefs.getBoolean("firstTime",true))
        {
            Intent startScreen = new Intent(getApplicationContext(),LoginActivity.class);
            startScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(startScreen);
            finish();
        }
    }

    public void openSearch() {

        search.setSearchString("");
        search.revealFromMenuItem(R.id.action_search, this);

        search.setMenuListener(new SearchBox.MenuListener() {

            @Override
            public void onMenuClick() {
            }

        });
        search.setSearchListener(new SearchBox.SearchListener() {

            @Override
            public void onSearchOpened() {
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }

            @Override
            public void onSearchClosed() {
                openSearch = false;
                closeSearch();
            }

            @Override
            public void onSearchTermChanged() {

            }

            @Override
            public void onSearch(String searchTerm) {
                if (searchTerm.equals("")) {
                    switch (menuString) {
                        case "Store":
                            toolbar.setTitle("Store");
                            ((BookAdapter) mStoreRecyclerView.getAdapter()).flushFilter();
                            break;
                        case "Class":
                            toolbar.setTitle("My Classes");
                            ((ClassAdapter) adapter4).flushFilter();
                            break;
                        case "Book":
                            toolbar.setTitle("My Books");
                            ((TeacherAdapter) adapter2).flushFilter();
                            break;
                        default:
                            toolbar.setTitle("SmartRead");
                            ((MainAdapter) listView.getAdapter()).flushFilter();
                            break;
                    }
                } else {
                    toolbar.setTitle(searchTerm);
                    switch (menuString) {
                        case "Store":
                            ((BookAdapter) mStoreRecyclerView.getAdapter()).setFilter(searchTerm);
                            break;
                        case "Class":
                            ((ClassAdapter) adapter4).setFilter(searchTerm);
                            break;
                        case "Book":
                            ((TeacherAdapter) adapter2).setFilter(searchTerm);
                            break;
                        default:
                            ((MainAdapter) listView.getAdapter()).setFilter(searchTerm);
                            break;
                    }
                }
            }

            @Override
            public void onSearchCleared() {
                switch (menuString) {
                    case "Store":
                        ((BookAdapter) mStoreRecyclerView.getAdapter()).flushFilter();
                        break;
                    case "Class":
                        ((ClassAdapter) adapter4).flushFilter();
                        break;
                    case "Book":
                        ((TeacherAdapter) adapter2).flushFilter();
                        break;
                    default:
                        ((MainAdapter) listView.getAdapter()).flushFilter();
                        break;
                }
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            if (requestCode == FILE_CODE && resultCode == Activity.RESULT_OK && data.getData().getPath().endsWith(".pdf")) {
                Uri uri = data.getData();
                teacherFile = new File(uri.getPath());
                pdf.invalidate();
                PDFMode = "Teacher";
                TextView title = (TextView) findViewById(R.id.pdfTitle);
                title.setText(teacherFile.getName().replace(".pdf", ""));
                pdf.fromFile(teacherFile)
                        .defaultPage(1)
                        .showMinimap(this.getSharedPreferences("com.teched.smartread", Context.MODE_PRIVATE).getBoolean("pref_minimap", false))
                        .enableSwipe(true)
                        .load();
                AnimateTeacher(true);
                try {
                    newObject = new JSONObject();
                    newObject.put("Finished", false);
                    newObject.put("Favorite", false);
                    newObject.put("LastPage", 0);
                    newObject.put("MaxPage", 0);
                    newObject.put("Date","");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 1234 && resultCode == RESULT_OK) {
                ArrayList<String> matches = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                search.populateEditText(matches);
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void mic(View v) {
        search.micClick(this);
    }

    protected void closeSearch() {
        search.hideCircularly(this);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        if(search.getSearchText().isEmpty()) {
            switch (menuString) {
                case "Library":
                    toolbar.setTitle("SmartRead");
                    break;
                case "Store":
                    toolbar.setTitle("Store");
                    break;
                case "Book":
                    toolbar.setTitle("My Books");
                    break;
                case "Class":
                    toolbar.setTitle("My Classes");
                    break;
                default:
                    toolbar.setTitle("SmartRead");
                    break;
            }
        }
    }

    public static String saveToInternalStorage(Context context, Bitmap bitmapImage, String name){
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath=new File(directory,name + ".jpg");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return directory.getAbsolutePath();
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void copyFile(String Path, String outputPath) {
        if (!Path.equals(outputPath)) {
            InputStream in;
            OutputStream out;
            try {
                in = new FileInputStream(Path);
                out = new FileOutputStream(outputPath, false);
                byte[] buffer = new byte[1024];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                in.close();
                out.flush();
                out.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    public void distribute() {
        if(isOnline() && currentBook.matches("^-?\\d+$")) {
            new AsyncJob.AsyncJobBuilder<Boolean>()
                    .doInBackground(new AsyncJob.AsyncAction<Boolean>() {
                        @Override
                        public Boolean doAsync() {
                            try {
                                distributeList.clear();
                                JSONArray Classes = new JSONObject(JsonClass.getJSON("http://php-smartread.rhcloud.com/get_user_classes.php?email=" + getApplicationContext().getSharedPreferences("com.teched.smartread", Context.MODE_PRIVATE).getString("Email", getString(R.string.profile_description)))).getJSONArray("classes");
                                for (int i = 0; i < Classes.length(); i++) {
                                    distributeList.add(new Class());
                                    JSONObject Class = new JSONObject(JsonClass.getJSON("http://php-smartread.rhcloud.com/get_class.php?id=" + Classes.getString(i)));
                                    Class currentClass = distributeList.get(distributeList.size() - 1);
                                    currentClass.name = Class.getString("name");
                                    currentClass.users = Class.getJSONArray("users");
                                    currentClass.id = Classes.getString(i);
                                }
                            }
                            catch(Exception e) {e.printStackTrace();}
                            return true;
                        }
                    })
                    .doWhenFinished(new AsyncJob.AsyncResultAction<Boolean>() {
                        @Override
                        public void onResult(Boolean result) {
                            adapter3.notifyDataSetChanged();
                            AnimateDistribute(true);
                        }
                    }).create().start();
        } else {
            showSnackbar(getResources().getString(!isOnline()?R.string.no_connection:R.string.no_sync_ready), Snackbar.LENGTH_SHORT);
        }
    }

    public void createClass() {
        final SharedPreferences prefs = this.getSharedPreferences("com.teched.smartread", Context.MODE_PRIVATE);
        final EditText input = new EditText(this);
        final AlertDialog alert = new AlertDialog.Builder(this)
                .setTitle("Create Class")
                .setMessage("Choose a name for your class")
                .setView(input)
                .setPositiveButton("Ok", null)
                .setNegativeButton("Cancel", null)
                .create();

        alert.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button b = alert.getButton(AlertDialog.BUTTON_POSITIVE);
                Button c = alert.getButton(AlertDialog.BUTTON_NEGATIVE);
                b.setTextColor(Color.rgb(68,138,255));
                c.setTextColor(Color.rgb(68,138,255));
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if(input.getText().toString().length()>0 && input.getText().toString().length()<30) {
                            try {
                                /*JSONObject books = */new JSONObject(JsonClass.getJSON("http://php-smartread.rhcloud.com/create_class.php?name=" + input.getText().toString() + "&teacher=" + prefs.getString("Email", getString(R.string.profile_description))));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            alert.dismiss();
                            refreshClass();
                        }
                    }
                });
            }
        });
        alert.show();
    }
    private void refreshClass() {
        new AsyncJob.AsyncJobBuilder<Boolean>()
                .doInBackground(new AsyncJob.AsyncAction<Boolean>() {
                    @Override
                    public Boolean doAsync() {
                        try {
                            classList.clear();
                            JSONArray Classes = new JSONObject(JsonClass.getJSON("http://php-smartread.rhcloud.com/get_user_classes.php?email=" + getApplication().getSharedPreferences("com.teched.smartread", Context.MODE_PRIVATE).getString("Email", getString(R.string.profile_description)))).getJSONArray("classes");
                            for (int i = 0; i < Classes.length(); i++) {
                                classList.add(new Class());
                                JSONObject Class = new JSONObject(JsonClass.getJSON("http://php-smartread.rhcloud.com/get_class.php?id=" + Classes.getString(i)));
                                Class currentClass = classList.get(classList.size() - 1);
                                currentClass.name = Class.getString("name");
                                currentClass.users = Class.getJSONArray("users");
                                currentClass.pending = Class.getJSONArray("pending");
                                currentClass.id = Classes.getString(i);
                                currentClass.access_code = Class.getString("access_code");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                })
                .doWhenFinished(new AsyncJob.AsyncResultAction<Boolean>() {
                    @Override
                    public void onResult(Boolean result) {
                        adapter4.notifyDataSetChanged();
                        ((ClassAdapter)adapter4).flushFilter();
                        if(refreshClasses.isRefreshing()) refreshClasses .setRefreshing(false);
                    }
                }).create().start();
    }
    private void refreshOverview() {
        new AsyncJob.AsyncJobBuilder<Boolean>()
                .doInBackground(new AsyncJob.AsyncAction<Boolean>() {
                    @Override
                    public Boolean doAsync() {
                        try {
                            overviewList.clear();
                            JSONArray Classes = new JSONObject(JsonClass.getJSON("http://php-smartread.rhcloud.com/get_books.php?email=" + getApplication().getSharedPreferences("com.teched.smartread", Context.MODE_PRIVATE).getString("Email", getString(R.string.profile_description)))).getJSONArray("teacher_books");
                            for (int i = 0; i < Classes.length(); i++) {
                                overviewList.add(new TeacherBook());
                                JSONObject Book = new JSONObject(JsonClass.getJSON("http://php-smartread.rhcloud.com/get_book_details.php?id=" + Classes.getString(i)));
                                TeacherBook currentClass = overviewList.get(overviewList.size() - 1);
                                currentClass.name = Book.getString("title");
                                currentClass.author = Book.getString("author");
                                currentClass.id = Classes.getString(i);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                })
                .doWhenFinished(new AsyncJob.AsyncResultAction<Boolean>() {
                    @Override
                    public void onResult(Boolean result) {
                        adapter7.notifyDataSetChanged();
                        ((TeacherAdapter) adapter7).flushFilter();
                        if(refreshOverview.isRefreshing()) refreshOverview.setRefreshing(false);
                    }
                }).create().start();
    }

    private void refreshUsers(final JSONArray Classes,final JSONArray Classes2) {
        new AsyncJob.AsyncJobBuilder<Boolean>()
            .doInBackground(new AsyncJob.AsyncAction<Boolean>() {
                @Override
                public Boolean doAsync() {
                    try {
                        usersList.clear();
                        for (int i = 0; i < Classes.length(); i++) {
                            usersList.add(new Users());
                            JSONObject Class = new JSONObject(JsonClass.getJSON("http://php-smartread.rhcloud.com/get_user.php?id=" + Classes.getString(i))).getJSONArray("users").getJSONObject(0);
                            Users currentClass = usersList.get(usersList.size() - 1);
                            currentClass.name = Class.getString("name");
                            currentClass.id = Classes.getString(i);
                            currentClass.email = Class.getString("email");
                            currentClass.profileUrl = Class.getString("profileUrl");
                        }
                        for (int i = 0; i < Classes2.length(); i++) {
                            usersList.add(new Users());
                            JSONObject Class = new JSONObject(JsonClass.getJSON("http://php-smartread.rhcloud.com/get_user.php?id=" + Classes2.getString(i))).getJSONArray("users").getJSONObject(0);
                            Users currentClass = usersList.get(usersList.size() - 1);
                            currentClass.name = Class.getString("name");
                            currentClass.id = Classes2.getString(i);
                            currentClass.email = Class.getString("email");
                            currentClass.profileUrl = Class.getString("profileUrl");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            })
                .doWhenFinished(new AsyncJob.AsyncResultAction<Boolean>() {
                    @Override
                    public void onResult(Boolean result) {
                        adapter5.notifyDataSetChanged();
                    }
                }).create().start();
    }

    private void refreshOverviewClasses(final String ID) {
        new AsyncJob.AsyncJobBuilder<Boolean>()
                .doInBackground(new AsyncJob.AsyncAction<Boolean>() {
                    @Override
                    public Boolean doAsync() {
                        try {
                            overviewClassesList.clear();
                            String[] Book = new JSONObject(JsonClass.getJSON("http://php-smartread.rhcloud.com/get_book_details.php?id=" + ID)).getString("classes").split(",");
                            for (String aBook : Book) {
                                JSONObject Class = new JSONObject(JsonClass.getJSON("http://php-smartread.rhcloud.com/get_class.php?id=" + aBook));
                                overviewClassesList.add(new Class());
                                Class currentClass = overviewClassesList.get(overviewClassesList.size() - 1);
                                currentClass.id = aBook;
                                currentClass.name = Class.getString("name");
                                currentClass.users = Class.getJSONArray("users");
                                currentClass.pending = Class.getJSONArray("pending");
                                currentClass.access_code = Class.getString("access_code");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                })
                .doWhenFinished(new AsyncJob.AsyncResultAction<Boolean>() {
                    @Override
                    public void onResult(Boolean result) {
                        adapter8.notifyDataSetChanged();
                        ((ClassAdapter)adapter8).flushFilter();
                        //if(refreshOverview.isRefreshing()) refreshOverview.setRefreshing(false);
                    }
                }).create().start();
    }

    private void joinClass() {
        final SharedPreferences prefs = this.getSharedPreferences("com.teched.smartread", Context.MODE_PRIVATE);
        final EditText input = new EditText(this);
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
        final AlertDialog alert = new AlertDialog.Builder(this)
                .setTitle("Join Class")
                .setMessage("Enter the access code for your class")
                .setView(input)
                .setPositiveButton("Ok", null)
                .setNegativeButton("Cancel", null)
                .create();

        alert.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button b = alert.getButton(AlertDialog.BUTTON_POSITIVE);
                Button c = alert.getButton(AlertDialog.BUTTON_NEGATIVE);
                b.setTextColor(Color.rgb(68,138,255));
                c.setTextColor(Color.rgb(68,138,255));
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if(input.getText().toString().length()>0) {
                            try {
                                jobManager.addJobInBackground(new JoinJob(prefs.getString("Email", getString(R.string.profile_description)), input.getText().toString().toLowerCase()));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            alert.dismiss();
                            refreshClass();
                        }
                    }
                });
            }
        });
        alert.show();
    }
    private void addUser() {
        final EditText input = new EditText(this);
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(320)});
        final AlertDialog alert = new AlertDialog.Builder(this)
                .setTitle("Add User")
                .setMessage("Enter the email of your user")
                .setView(input)
                .setPositiveButton("Ok", null)
                .setNegativeButton("Cancel", null)
                .create();

        alert.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button b = alert.getButton(AlertDialog.BUTTON_POSITIVE);
                Button c = alert.getButton(AlertDialog.BUTTON_NEGATIVE);
                b.setTextColor(Color.rgb(68,138,255));
                c.setTextColor(Color.rgb(68,138,255));
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if(input.getText().toString().length()>0) {
                            try {
                                if(isOnline()) {
                                    new AsyncJob.AsyncJobBuilder<Boolean>()
                                            .doInBackground(new AsyncJob.AsyncAction<Boolean>() {
                                                @Override
                                                public Boolean doAsync() {
                                                    JsonClass.getJSON("http://php-smartread.rhcloud.com/add_class_user_instant.php?email=" + input.getText().toString() + "&classid=" + currentClassId);
                                                    return true;
                                                }
                                            })
                                            .doWhenFinished(new AsyncJob.AsyncResultAction<Boolean>() {
                                                @Override
                                                public void onResult(Boolean result) {
                                                    refreshClass();
                                                    refreshUsers(currentClass,currentClassPending);
                                                }
                                            }).create().start();
                                }
                                else {
                                    jobManager.addJobInBackground(new AddJob(input.getText().toString(), currentClassId));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            alert.dismiss();
                        }
                    }
                });
            }
        });
        alert.show();
    }
    public static JSONArray RemoveJSONArray( JSONArray jarray,String value) {

        JSONArray Njarray = new JSONArray();
        try {
            for (int i = 0; i < jarray.length(); i++) {
                if (!jarray.getString(i).equals(value)) {
                    Njarray.put(jarray.get(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Njarray;
    }
    private int getPendingLength() {
        return currentClassPending.length();
    }
    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType("text/plain");
        } catch (Exception e) {
            e.printStackTrace();
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }
    private String readNFCText(NdefRecord record) throws UnsupportedEncodingException {

        byte[] payload = record.getPayload();

        String textEncoding;
        if((payload[0] & 128) == 0)
            textEncoding = "UTF-8";
        else
            textEncoding = "UTF-16";

        int languageCodeLength = payload[0] & 0063;

        return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
    }
    private void TagRead(final Tag tag) {
        AsyncJob.doInBackground(new AsyncJob.OnBackgroundJob() {
            @Override
            public void doOnBackground() {
                Ndef ndef = Ndef.get(tag);
                if (ndef != null) {

                    NdefMessage ndefMessage = ndef.getCachedNdefMessage();

                    NdefRecord[] records = ndefMessage.getRecords();
                    for (NdefRecord ndefRecord : records) {
                        if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                            try {
                                String result = readNFCText(ndefRecord);
                                if (result != null) {
                                    if (result.substring(0, 9).equals("SmartRead")) {
                                        jobManager.addJobInBackground(new AddBookJob(getApplicationContext().getSharedPreferences("com.teched.smartread", Context.MODE_PRIVATE).getString("Email", getString(R.string.profile_description)), result.substring(9)));
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                AsyncJob.doOnMainThread(new AsyncJob.OnMainThreadJob() {
                    @Override
                    public void doInUIThread() {
                        showSnackbar("Book added", Snackbar.LENGTH_SHORT);
                    }
                });
            }
        });
    }
    private void showSnackbar(String text, int length) {
        if(refreshLayout.getVisibility()==View.VISIBLE || store.getVisibility()==View.VISIBLE)
            Snackbar.make(findViewById(R.id.snackLayout), text, length).show();
        else if(teacher.getVisibility()==View.VISIBLE)
            Snackbar.make(teacher, text, length).show();
        else if(classes.getVisibility()==View.VISIBLE) {
            if(viewFlipper.getDisplayedChild()==0)
                Snackbar.make(findViewById(R.id.classListLayout), text, length).show();
            else
                Snackbar.make(findViewById(R.id.classUsersLayout), text, length).show();
        }
    }
    private void setupFloatingLabelError(final TextInputLayout floatingUsernameLabel, final String Error) {
        floatingUsernameLabel.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {
                if (text.length() < 1) {
                    floatingUsernameLabel.setError(Error);
                    floatingUsernameLabel.setErrorEnabled(true);
                } else {
                    floatingUsernameLabel.setErrorEnabled(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
    private void lockOrientation() {
        int rotation;
        switch (getResources().getConfiguration().orientation){
            case Configuration.ORIENTATION_PORTRAIT:
                rotation = getWindowManager().getDefaultDisplay().getRotation();
                if(rotation == android.view.Surface.ROTATION_90|| rotation == android.view.Surface.ROTATION_180){
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
                break;

            case Configuration.ORIENTATION_LANDSCAPE:
                rotation = getWindowManager().getDefaultDisplay().getRotation();
                if(rotation == android.view.Surface.ROTATION_0 || rotation == android.view.Surface.ROTATION_90){
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                }
                break;
        }
    }

    private void refreshStore() {
        new AsyncJob.AsyncJobBuilder<Boolean>()
                .doInBackground(new AsyncJob.AsyncAction<Boolean>() {
                    @Override
                    public Boolean doAsync() {
                        try {
                            booksList.clear();
                            JSONArray Books = new JSONObject(JsonClass.getJSON("http://php-smartread.rhcloud.com/get_all_books.php")).getJSONArray("books");
                            for(int i = 0; i<Books.length();i++) {
                                booksList.add(new Book());
                                JSONObject Book = new JSONObject(JsonClass.getJSON("http://php-smartread.rhcloud.com/get_book_details.php?id=" + Books.getString(i)));
                                Book currentBook = booksList.get(booksList.size() - 1);
                                currentBook.name = Book.getString("title");
                                currentBook.author = Book.getString("author");
                                currentBook.downloads = Book.getString("downloads");
                                currentBook.id = Books.getString(i);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                })
                .doWhenFinished(new AsyncJob.AsyncResultAction<Boolean>() {
                    @Override
                    public void onResult(Boolean result) {

                        ((BookAdapter) mStoreRecyclerView.getAdapter()).flushFilter();
                        if (refreshBooks.isRefreshing()) {
                            toolbar.setTitle("Store");
                            refreshBooks.setRefreshing(false);
                        }
                    }
                }).create().start();
    }
    private void refreshJSON() {
        final File folder = new File(Path2);
        new AsyncJob.AsyncJobBuilder<Boolean>()
                .doInBackground(new AsyncJob.AsyncAction<Boolean>() {
                    @Override
                    public Boolean doAsync() {
                        File[] files = folder.listFiles();
                        if (files != null) {
                            for (File file : files)
                                if (file.getName().endsWith(".json")) {
                                    try {
                                        JsonClass.DownloadJSON(Path4, file.getName(), getApplicationContext().getSharedPreferences("com.teched.smartread", Context.MODE_PRIVATE).getString("Email", getString(R.string.profile_description)));
                                        JSONObject object = new JSONObject(readFromFile(file.getPath()));
                                        JSONObject object2 = new JSONObject(readFromFile(Path4 + file.getName()));
                                        if (object2.getInt("MaxPage") > object.getInt("MaxPage")) {
                                            String name = file.getName();
                                            file.delete();
                                            copyFile(Path4 + name, Path2 + name);
                                            new File(Path4 + name).delete();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                        }
                        return true;
                    }
                }).create().start();
    }
    private int bookAnswerRate(JSONObject mainObject2) {
        try {
            int entries = 0;
            float sum = 0;
            for (int i = 0; i <= mainObject2.getInt("MaxPage") + 1; i++) {
                if (!mainObject2.isNull(String.valueOf(i))) {
                    JSONArray jsonArray = mainObject2.getJSONArray(String.valueOf(i));
                    if(jsonArray.getBoolean(0)) {
                        for (int j = 2; !jsonArray.isNull(j); j++) {
                            sum += 1f / ((float) jsonArray.getJSONObject(j).getInt("Trials") + 1) * 100;
                            entries++;
                        }
                    }
                }
            }
            return (entries==0?0:(int)sum/entries);
        } catch (Exception e) {e.printStackTrace(); }
        return 0;
    }
    private void refreshStatistics() {
        new AsyncJob.AsyncJobBuilder<Boolean>()
                .doInBackground(new AsyncJob.AsyncAction<Boolean>() {
                    @Override
                    public Boolean doAsync() {
                        try {
                            topList.clear();
                            questionsList.clear();
                            questionTable = new Question[1000][1000];
                            maxPage = 0;
                            maxQuestions = 0;
                            JSONArray Classes = new JSONObject(JsonClass.getJSON("http://php-smartread.rhcloud.com/get_class.php?id=" + currentOverviewClass)).getJSONArray("users");
                            for(int i = 0; i<Classes.length();i++) {
                                if(exists(new URL("http://php-smartread.rhcloud.com/books/" + Classes.getString(i) + "_" + currentOverviewBook + ".json"))) {
                                    topList.add(new Users());
                                    Users currentUser = topList.get(topList.size() - 1);
                                    JSONObject currentJSON = new JSONObject(JsonClass.getJSON("http://php-smartread.rhcloud.com/books/" + Classes.getString(i) + "_" + currentOverviewBook + ".json"));
                                    JSONObject Class = new JSONObject(JsonClass.getJSON("http://php-smartread.rhcloud.com/get_user.php?id=" + Classes.getString(i))).getJSONArray("users").getJSONObject(0);
                                    currentUser.name = Class.getString("name");
                                    currentUser.id = Classes.getString(i);
                                    currentUser.email = Class.getString("email");
                                    currentUser.profileUrl = Class.getString("profileUrl");
                                    currentUser.answerRate = bookAnswerRate(currentJSON);
                                    currentUser.maxPage = currentJSON.getString("MaxPage");
                                    currentUser.finished = currentJSON.getBoolean("Finished");
                                    for (int j = 0; j <= currentJSON.getInt("MaxPage") + 1; j++) {
                                        if (!currentJSON.isNull(String.valueOf(j))) {
                                            JSONArray jsonArray = currentJSON.getJSONArray(String.valueOf(j));
                                            if(!jsonArray.getBoolean(0)) break;
                                            if(maxPage<j) maxPage=j;
                                            for(int l = 2;!jsonArray.isNull(l);l++) {
                                                questionTable[j][l] = new Question();
                                                questionTable[j][l].sum += 1f / ((float)jsonArray.getJSONObject(l).getInt("Trials")+1) * 100;
                                                questionTable[j][l].entries++;
                                                questionTable[j][l].QuestionString = jsonArray.getJSONObject(l).getString("Question");
                                                if(l>maxQuestions) maxQuestions = l;
                                            }
                                        }
                                    }
                                }
                            }
                            for(int j = 0;j<=maxPage;j++) {
                                for(int l = 2; l<=maxQuestions;l++) {
                                    try {
                                        Question currentQuestion = new Question();
                                        currentQuestion.QuestionString = questionTable[j][l].QuestionString;
                                        currentQuestion.entries = questionTable[j][l].entries;
                                        currentQuestion.sum = questionTable[j][l].sum;
                                        questionsList.add(currentQuestion);
                                    }
                                    catch (Exception e) {e.printStackTrace();}
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                })
                .doWhenFinished(new AsyncJob.AsyncResultAction<Boolean>() {
                    @Override
                    public void onResult(Boolean result) {
                        int sum = 0;
                        int sum2 = 0;
                        for(Users aUser: topList)
                            sum+=aUser.answerRate;
                        for(Question aUser: questionsList)
                            sum2+=aUser.sum/ aUser.entries;
                        ((ArcProgress)findViewById(R.id.arc_progress)).setProgress(topList.size()!=0?sum/topList.size():0);
                        ((ArcProgress)findViewById(R.id.arc_progress2)).setProgress(questionsList.size()!=0?sum2/questionsList.size():0);
                        adapter9.notifyDataSetChanged();
                        adapter10.notifyDataSetChanged();
                        adapter9.flushFilter();
                        adapter10.flushFilter();
                    }
                }).create().start();
    }
    public boolean exists(URL url) {
        try {
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            int responseCode = huc.getResponseCode();

            return responseCode == 200;
        } catch (Exception e) {e.printStackTrace(); }
        return false;
    }
}