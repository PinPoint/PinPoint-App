package de.pinpoint.app;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

import de.pinpoint.app.marker.MarkerIconCreator;
import de.pinpoint.client.dataprovider.UpdateListener;
import de.pinpoint.client.locationclient.PinPointPosition;
import de.pinpoint.client.locationclient.UserInfo;

public class MapFragment extends Fragment implements UpdateListener {

    private MapView map;
    private HashMap<UUID, Marker> markerByUuid = new HashMap<>();
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_map, parent, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        context = getActivity().getApplicationContext();

        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));
        map = (MapView) view.findViewById(R.id.map);
        map.setBuiltInZoomControls(false);
        map.setMultiTouchControls(true);
        map.setTileSource(TileSourceFactory.MAPNIK);

        /*
        RotationGestureOverlay mRotationGestureOverlay = new RotationGestureOverlay(context, map);
        mRotationGestureOverlay.setEnabled(true);
        map.getOverlays().add(mRotationGestureOverlay);
         */

        IMapController mapController = map.getController();
        mapController.setZoom(9.5);
        GeoPoint startPoint = new GeoPoint(52.516275, 13.377704);
        mapController.setCenter(startPoint);

        MyLocationNewOverlay mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(context), map);
        mLocationOverlay.enableMyLocation();
        map.getOverlays().add(mLocationOverlay);

        markerByUuid.clear();
    }

    @Override
    public void onUpdate(Collection<UserInfo> collection) {
        Handler mainHandler = new Handler(PinPoint.getmAppContext().getMainLooper());
        mainHandler.post(() -> this.updateMarkers(collection));
    }

    private void updateMarkers(Collection<UserInfo> collection) {
        if (map == null)
            return;

        Collection<Marker> toRemove = new ArrayList<>();
        toRemove.addAll(markerByUuid.values());

        for (UserInfo info: collection) {
            Marker marker = getOrCreateMarker(info);
            PinPointPosition position = info.getPosition();
            marker.setPosition(new GeoPoint(position.getLatitude(), position.getLongitude()));
            this.updateIcon(marker, info);
            toRemove.remove(marker);
        }

        map.getOverlays().removeAll(toRemove);
    }

    private Marker getOrCreateMarker(UserInfo info) {
        UUID uuid = info.getUuid();
        Marker marker;
        if (markerByUuid.containsKey(uuid)) {
            marker = markerByUuid.get(uuid);
        } else {
            marker = createMarker(info);
            markerByUuid.put(uuid, marker);
        }
        //marker.setTextIcon(info.getName());
        return marker;

    }

    private Marker createMarker(UserInfo info) {
        Marker marker = new Marker(map);
        this.updateIcon(marker, info);
        map.getOverlays().add(marker);

        return marker;
    }

    private void updateIcon(Marker marker, UserInfo info) {
        MarkerIconCreator creator = new MarkerIconCreator();
        creator.setColor(info.getColor());
        creator.setName(info.getName());

        marker.setIcon(creator.createIcon(context));

        marker.setAnchor(0.5f, 0.92f);

        String name = info.getName();
        String color = info.getColor();
        double latitude = info.getPosition().getLatitude();
        double longitude = info.getPosition().getLongitude();

        String format = "Name: %s\nColor: %s\nLatitude: %.4f\nLongitude: %.4f";
        String title = String.format(format, name, color, latitude, longitude);

        marker.setTitle(title);
    }
}
