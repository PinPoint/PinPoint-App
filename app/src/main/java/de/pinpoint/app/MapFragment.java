package de.pinpoint.app;

import android.content.Context;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import de.pinpoint.app.logic.GPSException;
import de.pinpoint.app.logic.Logic;
import de.pinpoint.app.marker.MarkerIconCreator;
import de.pinpoint.app.util.PositionUtil;
import de.pinpoint.client.dataprovider.UpdateListener;
import de.pinpoint.client.locationclient.PinPointPosition;
import de.pinpoint.client.locationclient.UserInfo;

public class MapFragment extends Fragment implements UpdateListener {
    private MapView map;
    private HashMap<UUID, Marker> markerByUuid = new HashMap<>();
    private Context context;
    private UserInfo selectedUser;

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
        MyLocationNewOverlay mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(context), map);
        mLocationOverlay.enableMyLocation();
        map.getOverlays().add(mLocationOverlay);
        markerByUuid.clear();

        if (this.selectedUser != null) {
            zoomToUser(this.selectedUser);
            this.selectedUser = null;
        } else {
            this.zoomToOwn();
        }

        Logic logic = PinPoint.getLogic();
        if (logic.isUpdaterRunning()) {
            Collection<UserInfo> info = logic.getUsers();
            this.updateMarkers(info);
        }
    }

    private void zoomToOwn() {
        GeoPoint zoomPoint;
        try {
            PinPointPosition ownPosition = PinPoint.getLogic().getOwnPosition();
            zoomPoint = PositionUtil.toGeoPoint(ownPosition);
        } catch (GPSException e) {
            zoomPoint = new GeoPoint(52.516275, 13.377704);
        }
        IMapController mapController = map.getController();
        mapController.setZoom(9.5);
        mapController.setCenter(zoomPoint);
    }

    private void zoomToUser(UserInfo info) {
        GeoPoint zoomPoint = PositionUtil.toGeoPoint(info.getPosition());
        IMapController mapController = map.getController();
        mapController.setZoom(19.0);
        mapController.setCenter(zoomPoint);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.map = null;
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

        for (UserInfo info : collection) {
            Marker marker = getOrCreateMarker(info);
            PinPointPosition position = info.getPosition();
            marker.setPosition(PositionUtil.toGeoPoint(position));
            this.updateIcon(marker, info);
            toRemove.remove(marker);
        }

        Iterator<Map.Entry<UUID, Marker>> itr = this.markerByUuid.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<UUID, Marker> entry = itr.next();
            Marker marker = entry.getValue();
            if (toRemove.contains(marker)) {
                map.getOverlays().remove(marker);
                itr.remove();
            }
        }
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

    public void setSelectedUser(UserInfo info) {
        this.selectedUser = info;
    }
}
