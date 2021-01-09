package de.pinpoint.app.logic;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import de.pinpoint.app.PinPoint;
import de.pinpoint.app.util.Callback;
import de.pinpoint.client.dataprovider.DataProvider;
import de.pinpoint.client.locationclient.LocationClient;

public class DataUpdater implements Runnable {

    private final DataProvider provider;
    private boolean running;
    private Thread updaterThread;
    private LocationClient client;
    private Callback<InternetException> internetExceptionHandler;
    private Callback<GPSException> gpsExceptionHandler;

    public DataUpdater(LocationClient client, DataProvider provider) {
        this.provider = provider;
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
        while (this.running) {
            this.postUpdate();
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(5));
            } catch (InterruptedException e) {

            }
        }
    }

    public void postUpdate() {
        try {
            client.postInfo(PinPoint.getLogic().getUserInfo());
            provider.invokeUpdate();
        } catch (IOException e) {
            this.internetExceptionHandler.call(new InternetException(e.getMessage(), e));
        } catch (GPSException e) {
            this.gpsExceptionHandler.call(e);
        }
    }

    public void start() {
        if (running) {
            return;
        }
        updaterThread = new Thread(this);
        updaterThread.start();
        this.running = true;
    }

    public void stop() {
        if (running) {
            this.running = false;
            updaterThread.interrupt();
        }
    }

    public boolean isRunning() {
        return running;
    }
}
