package de.pinpoint.app;

import android.app.Application;
import android.content.Context;

import de.pinpoint.app.logic.Logic;

public class PinPoint extends Application {
    private static Context mAppContext;
    private static Logic logic;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = getApplicationContext();
        logic = new Logic(mAppContext);
    }

    public static Logic getLogic() {
        return logic;
    }
}
