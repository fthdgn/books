package tr.name.fatihdogan.books.activity;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.content.Intent;
import android.support.annotation.CallSuper;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;

import tr.name.fatihdogan.books.callback.ActivityResultListener;

public abstract class BaseActivity extends AppCompatActivity implements LifecycleRegistryOwner {

    //region LifecycleRegistryOwner
    private final LifecycleRegistry mRegistry = new LifecycleRegistry(this);

    @Override
    public LifecycleRegistry getLifecycle() {
        return mRegistry;
    }
    //endregion

    //region ActivityResultListener
    private final SparseArray<ActivityResultListener> activityResultListeners = new SparseArray<>();
    private int listeners_last_index = -1;

    @Override
    @CallSuper
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ActivityResultListener listener = getActivityResultListener(requestCode);
        if (listener != null)
            listener.onActivityResult(resultCode, data);
        removeActivityResultListener(requestCode);
    }

    /**
     * Starts an activity intent for result.
     * When result received the listener will be called.
     *
     * @param intent   Intent which will be started
     * @param listener The listener which will be called when result is received
     */
    public void startActivityForResult(@NonNull Intent intent, @NonNull ActivityResultListener listener) {
        startActivityForResult(intent, addActivityResultListener(listener));
    }

    /**
     * Add a activity result listener and returns request code.
     * Use {@link BaseActivity#startActivityForResult(Intent, ActivityResultListener)} if possible.
     * If it is not possible use returned request code to start intent.
     *
     * @param listener Listener which will be called
     * @return Request code
     */
    public int addActivityResultListener(@NonNull ActivityResultListener listener) {
        /*
         * Small possibility.
         * Probably there are a few listeners never removed.
         * They will be overwritten.
         */
        if (listeners_last_index == 0xffff)
            listeners_last_index = -1;

        activityResultListeners.put(++listeners_last_index, listener);
        return listeners_last_index;
    }

    /**
     * Result listener for given request code.
     *
     * @param requestCode Request code (form 0 to 65535 (0xffff))
     * @return Result listener
     */
    private ActivityResultListener getActivityResultListener(@IntRange(from = 0, to = 0xffff) int requestCode) {
        return activityResultListeners.get(requestCode);
    }

    /**
     * Removes result listener for given request code
     *
     * @param requestCode Request code (form 0 to 65535 (0xffff))
     */
    private void removeActivityResultListener(@IntRange(from = 0, to = 0xffff) int requestCode) {
        activityResultListeners.remove(requestCode);
        if (activityResultListeners.size() == 0)
            listeners_last_index = -1;
    }
    //endregion

}
