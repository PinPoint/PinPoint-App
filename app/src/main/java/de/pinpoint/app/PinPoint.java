package de.pinpoint.app;

import android.app.Application;
import android.content.Context;

import de.pinpoint.app.logic.Logic;
import de.pinpoint.app.logic.UIAccess;

public class PinPoint extends Application {
    private static Context mAppContext;
    private static Logic logic;

    private static UIAccess uiAccess;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = getApplicationContext();
        logic = new Logic(mAppContext);
        uiAccess = new UIAccess(mAppContext);
    }

    public static Logic getLogic() {
        return logic;
    }

    public static UIAccess getUiAccess() {
        return uiAccess;
    }
}
