package com.ume.snackbar;

import android.view.ViewGroup;

import com.google.android.material.snackbar.BaseTransientBottomBar;

public interface SnackBarManager {
    int LENGTH_LONGER = 10000;

    boolean remove(BaseTransientBottomBar bar);

    void show(BaseTransientBottomBar bar);

    ViewGroup getContainer();
}
