package com.ume.snackbar;

import android.view.ViewGroup;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.BaseTransientBottomBar.BaseCallback;

import java.util.HashSet;

import static com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE;

public class FragmentSnackBarManager extends BaseCallback<BaseTransientBottomBar>
        implements SnackBarManager {
    private SnackBarManager parent;
    private HashSet<BaseTransientBottomBar> barSet;
    private BaseTransientBottomBar sticky;

    public FragmentSnackBarManager(SnackBarManager parent) {
        this.parent = parent;
        barSet = new HashSet<>();
    }

    public void dismissAll() {
        for (BaseTransientBottomBar bar : barSet) {
            bar.dismiss();
            parent.remove(bar);
        }
        if (sticky != null)
            sticky.dismiss();
        sticky = null;
        barSet.clear();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onDismissed(BaseTransientBottomBar transientBottomBar, int event) {
        super.onDismissed(transientBottomBar, event);
        transientBottomBar.removeCallback(this);
        barSet.remove(transientBottomBar);
    }

    @Override
    public boolean remove(BaseTransientBottomBar bar) {
        return parent.remove(bar) && barSet.remove(bar);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void show(BaseTransientBottomBar bar) {
        if (bar.getDuration() == LENGTH_INDEFINITE)
            sticky = bar;
        bar.addCallback(this);
        barSet.add(bar);
        parent.show(bar);
    }

    @Override
    public ViewGroup getContainer() {
        return parent.getContainer();
    }
}
