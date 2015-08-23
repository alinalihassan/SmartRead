package com.teched.smartread;

import android.os.Environment;

import com.nononsenseapps.filepicker.AbstractFilePickerActivity;
import com.nononsenseapps.filepicker.AbstractFilePickerFragment;

import java.io.File;

/**
 * All this class does is return a suitable fragment.
 */
public class FilteredFilePickerActivity extends AbstractFilePickerActivity {

    public FilteredFilePickerActivity() {
        super();
    }
    FilteredFilePickerFragment fragment;
    @Override
    protected AbstractFilePickerFragment<File> getFragment(
            final String startPath, final int mode, final boolean allowMultiple,
            final boolean allowCreateDir) {
        fragment = new FilteredFilePickerFragment();
        fragment.setArgs(startPath != null ? startPath : Environment.getExternalStorageDirectory().getPath(),
                mode, allowMultiple, allowCreateDir);
        return fragment;
    }
    @Override
    public void onBackPressed() {
        // If at top most level, normal behaviour
        if (fragment.isBackTop()) {
            super.onBackPressed();
        } else {
            // Else go up
            fragment.goUp();
        }
    }
}