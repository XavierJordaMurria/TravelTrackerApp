package cat.jorda.traveltrack.dayDetails;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import com.google.android.gms.maps.*;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

import cat.jorda.traveltrack.AddTripActivity;
import cat.jorda.traveltrack.R;
import cat.jorda.traveltrack.model.CustomMarker;
import cat.jorda.traveltrack.model.DayInfo;
import cat.jorda.traveltrack.model.TripInfo;

/**
 * Created by xj1 on 22/08/2017.
 */

public class MapDayFragment extends Fragment implements OnMapReadyCallback, LocationListener
{
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 00001;
    private static String TAG = FinancesDayFragment.class.getSimpleName();

    private SupportMapFragment mSupportMapFragment;

    private GoogleMap map_;
    boolean movedByApi_ = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

//        if(context instanceof DayListFragment.ItemSelectedListener)  // context instanceof YourActivity
//            this.listener_ = (DayListFragment.ItemSelectedListener) context; // = (YourActivity) context
//        else
//            throw new ClassCastException(context.toString()
//                    + " must implement StepsFragment.OnItemSelectedListener");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(cat.jorda.traveltrack.R.layout.map_details, container, false);

        mSupportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        if (mSupportMapFragment == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mSupportMapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.mapView, mSupportMapFragment).commit();
        }

        if (mSupportMapFragment != null)
            mSupportMapFragment.getMapAsync(this);

        getCurrentLocation();

        return rootView;
    }

    private boolean requestedMapPermission() {

        boolean permissionRequested = false;

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
            permissionRequested = true;
        }

        return permissionRequested;
    }

    @SuppressWarnings({"MissingPermission"})
    private Location getCurrentLocation()
    {
        if (requestedMapPermission())
            return null;

        LocationManager locManager;
        // Use the location manager through GPS
        locManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        //get the current location (last known location) from the location manager
        Location location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        //when the current location is found – stop listening for updates (preserves battery)
        locManager.removeUpdates(this);

        return location;
    }

    private void mapReadyLogic(GoogleMap map)
    {
        if (requestedMapPermission())
            return;

        map.getUiSettings().setAllGesturesEnabled(true);

        // Add a marker in Sydney and move the camera
        // This is a mock location so the camera animation looks nicer.
        LatLng marker_latlng = new LatLng(-34, 151);
        map.moveCamera(CameraUpdateFactory.newLatLng(marker_latlng));

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        MapsInitializer.initialize(this.getActivity());

        Location location = getCurrentLocation();
        //if location found display as a toast the current latitude and longitude
        if (location != null)
            Log.d(TAG, "Current location is, lat: " + location.getLatitude() + " long: " + location.getLongitude());
        else
            Log.w(TAG, "Couldn't get current location this time.");

       // Updates the location and zoom of the MapView
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 10);
        map.animateCamera(cameraUpdate);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case MY_PERMISSIONS_REQUEST_LOCATION:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    // permission was granted, yay!
                    mapReadyLogic(map_);
                }
                else
                {
                    // permission denied, boo!
                }

                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        if (googleMap != null)
        {
            map_ = googleMap;
            mapReadyLogic(map_);

            map_.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng)
                {
                    Marker newmarker = map_.addMarker(new MarkerOptions().position(latLng).title("marker title").icon(BitmapDescriptorFactory.fromResource(R.drawable.map_markers)));

//                    map_.addMarker(new MarkerOptions().position(latLng));
                }
            });
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
