package de.pinpoint.app.logic;

import android.os.Handler;
import android.provider.ContactsContract;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import de.pinpoint.app.PinPoint;
import de.pinpoint.client.dataprovider.DataProvider;
import de.pinpoint.client.locationclient.LocationClient;
import de.pinpoint.client.locationclient.RestClientFactory;

public class DataUpdater extends TimerTask {

    private final DataProvider provider;
    private boolean running;
    private Timer timer;
    private LocationClient client;
    private Callback<InternetException> internetExceptionHandler;
    private Callback<GPSException> gpsExceptionHandler;

    public DataUpdater(LocationClient client, DataProvider provider) {
        this.provider = provider;
        this.timer = new Timer();
        this.internetExceptionHandler = Exception::printStackTrace;
        this.gpsExceptionHandler = Exception::printStackTrace;
        this.client = client;
    }

    public void setInternetExceptionHandler(Callback<InternetException> internetExceptionHandler) {
        this.internetExceptionHandler = internetExceptionHandler;
    }

    public void setGpsExceptionHandler(Callback<GPSException> gpsExceptionHandler) {
        this.gpsExceptionHandler = gpsExceptionHandler;
    }

    @Override
    public void run() {
        try {
            provider.invokeUpdate();
            client.postInfo(PinPoint.getLogic().getUserInfo());
        } catch (IOException e) {
            this.internetExceptionHandler.call(new InternetException(e.getMessage(), e));
        } catch(GPSException e){
            this.gpsExceptionHandler.call(e);
        }
    }

    public void start() {
        if (running) {
            return;
        }
        timer.schedule(this, 0, TimeUnit.SECONDS.toMillis(10));
        this.running = true;
    }

    public void stop() {
        if (running) {
            timer.cancel();
            this.running = false;
        }
    }


}
