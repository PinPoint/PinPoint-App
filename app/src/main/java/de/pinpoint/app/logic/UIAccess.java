package de.pinpoint.app.logic;

import android.content.Context;

import androidx.appcompat.content.res.AppCompatResources;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import de.pinpoint.app.R;

public class UIAccess {
    private final Context mAppContext;
    private FloatingActionButton fab;

    public UIAccess(Context mAppContext) {
        this.mAppContext = mAppContext;
    }

    public FloatingActionButton getFab() {
        return fab;
    }

    public void setFab(FloatingActionButton fab) {
        this.fab = fab;
    }

    public void setButtonEnabled() {
        fab.setBackgroundTintList(AppCompatResources.getColorStateList(mAppContext, R.color.colorSuccess));
    }

    public void setButtonDisabled() {
        fab.setBackgroundTintList(AppCompatResources.getColorStateList(mAppContext, R.color.colorError));
    }

}
