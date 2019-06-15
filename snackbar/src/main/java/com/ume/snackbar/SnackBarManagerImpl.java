package com.ume.snackbar;

import android.view.ViewGroup;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.BaseTransientBottomBar.BaseCallback;

import java.util.LinkedList;

import static androidx.core.view.ViewCompat.postOnAnimationDelayed;
import static com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE;

public class SnackBarManagerImpl extends BaseCallback<BaseTransientBottomBar>
        implements SnackBarManager {
    private LinkedList<BaseTransientBottomBar> bars;
    private BaseTransientBottomBar showing;
    private ViewGroup container;

    public SnackBarManagerImpl(ViewGroup container) {
        this.container = container;
        bars = new LinkedList<>();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onDismissed(BaseTransientBottomBar dismissed, int event) {
        super.onDismissed(dismissed, event);
        dismissed.removeCallback(this);
        checkEquals(dismissed);
        show();
    }

    @Override
    public ViewGroup getContainer() {
        return container;
    }

    @Override
    public boolean remove(BaseTransientBottomBar bar) {
        return bars.remove(bar);
    }

    @Override
    public synchronized void show(BaseTransientBottomBar bar) {
        bars.add(bar);
        if (showing == null)
            show();
    }

    private void checkEquals(BaseTransientBottomBar bar) {
        if (showing != bar)
            throw new IllegalStateException("showing invalid");
    }

    @SuppressWarnings("unchecked")
    private synchronized void show() {
        showing = bars.poll();
        if (showing != null) {
            showing.addCallback(this);
            showing.show();
            if (indefinite(showing))
                loop();
        }
    }

    private void loop() {
        postOnAnimationDelayed(container, () -> {
            BaseTransientBottomBar next = bars.peekFirst();
            if (next != null) {
                BaseTransientBottomBar temp = showing;
                showing.dismiss();
                if (!indefinite(next))
                    show(temp);
            } else if (showing != null && indefinite(showing))
                loop();
        }, 5000);
    }

    private boolean indefinite(BaseTransientBottomBar bar) {
        return bar.getDuration() == LENGTH_INDEFINITE;
    }
}
