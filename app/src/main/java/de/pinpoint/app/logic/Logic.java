package de.pinpoint.app.logic;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;

import java.io.IOException;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.pinpoint.app.PinPoint;
import de.pinpoint.app.UserInfoAdapter;
import de.pinpoint.app.preferencestorage.KeyNotFoundException;
import de.pinpoint.app.preferencestorage.PreferenceStorage;
import de.pinpoint.client.dataprovider.DataProvider;
import de.pinpoint.client.dataprovider.UpdateListener;
import de.pinpoint.client.locationclient.LocationClient;
import de.pinpoint.client.locationclient.PinPointPosition;
import de.pinpoint.client.locationclient.RestClientFactory;
import de.pinpoint.client.locationclient.UserInfo;

public class Logic {
    private Context context;
    private PreferenceStorage prefStorage;
    private DataProvider provider;
    private PositionProvider positionProvider;
    private DataUpdater updater;
    private UserInfoAdapter uAdapter;

    public Logic(Context context) {
        this.context = context;
        this.prefStorage = new PreferenceStorage(context);
        this.uAdapter = new UserInfoAdapter(context);
        try {
            setTheme(prefStorage.getTheme());
        } catch (KeyNotFoundException ignored) {
        }
        this.positionProvider = new PositionProvider(context);
        RestClientFactory factory = new RestClientFactory();
        LocationClient client = factory.produceRestClient("https://thedst.de/pinpoint/api/v1/");
        this.provider = new DataProvider(client);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            while (true)
                try {
                    this.initializeConnection(client);
                    break;
                } catch (IOException e) {
                    noInternet(new InternetException("could not initialize first connection", e));
                }
        });
    }

    private void initializeConnection(LocationClient client) throws IOException {
        UUID uuid;
        if (this.prefStorage.existsUUID()) {
            uuid = this.prefStorage.getUUID();
        } else {
            uuid = client.getNewUuid();
            this.prefStorage.setUUID(uuid);
        }
        provider = new DataProvider(client, uuid);
        provider.addUpdateListener(uAdapter);

        updater = new DataUpdater(client, provider);
        updater.setInternetExceptionHandler(this::noInternet);
        updater.setGpsExceptionHandler(this::noGPS);
    }

    private void noInternet(InternetException cause) {
        cause.printStackTrace();
        System.out.printf("Internet Error: %s: %s", cause.getClass().getSimpleName(), cause.getMessage());
        Handler mainHandler = new Handler(context.getMainLooper());
        mainHandler.post(() -> {
            String text = "No internet connection";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        });
        this.stopUpdater();
    }

    private void noGPS(GPSException cause) {
        System.out.printf("GPS Error: %s: %s", cause.getClass().getSimpleName(), cause.getMessage());
        Handler mainHandler = new Handler(context.getMainLooper());
        mainHandler.post(() -> {
            String text = "No GPS";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        });
        this.stopUpdater();
    }

    public UserInfo getUserInfo() throws GPSException {
        UUID uuid = prefStorage.getUUID();
        String name = prefStorage.getName();
        String color = prefStorage.getColor();
        PinPointPosition position = this.positionProvider.getPosition();
        UserInfo info = new UserInfo(uuid, name, color, position);
        return info;
    }

    public void changePreferences(String name, String color, int theme) {
        prefStorage.setName(name);
        prefStorage.setColor(color);
        prefStorage.setTheme(theme);
        setTheme(theme);
    }

    public void startUpdater() {
        if (updater == null)
            return;
        PinPoint.getUiAccess().setButtonEnabled();
        updater.start();
    }

    public void stopUpdater() {
        PinPoint.getUiAccess().setButtonDisabled();
        if (updater == null)
            return;
        this.updater.stop();
        this.provider.clearCache();
    }

    public int getTheme() {
        try {
            return prefStorage.getTheme();
        } catch (KeyNotFoundException e) {
            return 0;
        }
    }

    private void setTheme(int theme) {
        if (theme == 0) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        } else if (theme == 1) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public String getName() {
        try {
            return prefStorage.getName();
        } catch (KeyNotFoundException e) {
            return "";
        }
    }

    public String getColor() {
        try {
            return prefStorage.getColor();
        } catch (KeyNotFoundException e) {
            return "#f44336";
        }
    }

    public boolean isUpdaterRunning() {
        if(updater == null){
            return false;
        }
        return updater.isRunning();
    }

    public UserInfoAdapter getUAdapter() {
        return uAdapter;
    }

    public void addUpdateListener(UpdateListener listener) {
        provider.addUpdateListener(listener);
    }

    public PinPointPosition getOwnPosition() throws GPSException {
        return this.positionProvider.getPosition();
    }

    public Collection<UserInfo> getUsers() {
        return provider.getUsers();
    }

    public void setAContext(Context aContext) {
        uAdapter.setAContext(aContext);
    }
}
