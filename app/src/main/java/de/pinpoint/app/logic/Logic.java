package de.pinpoint.app.logic;

import android.content.Context;

import androidx.appcompat.app.AppCompatDelegate;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.pinpoint.app.PinPoint;
import de.pinpoint.app.UserInfoAdapter;
import de.pinpoint.app.preferencestorage.KeyNotFoundException;
import de.pinpoint.app.preferencestorage.PreferenceStorage;
import de.pinpoint.client.dataprovider.DataProvider;
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
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            while(true)
            try {
                this.initializeConnection();
                break;
            } catch (IOException e) {
                noInternet(new InternetException("could not initialize first connection", e));
            }
        });
    }

    private void initializeConnection() throws IOException {
        RestClientFactory factory = new RestClientFactory();
        LocationClient client = factory.produceRestClient("https://thedst.de/pinpoint/api/v1/");
        UUID uuid;
        if(this.prefStorage.existsUUID()){
            uuid = this.prefStorage.getUUID();
        } else {
            uuid = client.getNewUuid();
            this.prefStorage.setUUID(uuid);
        }
        provider = new DataProvider(client, uuid);
        provider.setUpdateListener(uAdapter);

        updater = new DataUpdater(client, provider);
        updater.setInternetExceptionHandler(this::noInternet);
        updater.setGpsExceptionHandler(this::noGPS);
    }

    private void noInternet(InternetException cause){
        System.out.println("Server error: " + cause.getClass().getName());
        // TODO popup

        this.stopUpdater();
    }

    private void noGPS(GPSException cause){
        System.out.println("GPS error: " + cause.getClass().getName());
        // TODO popup
        cause.printStackTrace();

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
        updater.stop();
    }

    public int getTheme() {
        try {
            return prefStorage.getTheme();
        } catch (KeyNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
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

    private void setTheme(int theme) {
        if (theme == 0)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        else if (theme == 1)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    public boolean isUpdaterRunning() {
        return updater.isRunning();
    }

    public UserInfoAdapter getUAdapter() {
        return uAdapter;
    }
}
