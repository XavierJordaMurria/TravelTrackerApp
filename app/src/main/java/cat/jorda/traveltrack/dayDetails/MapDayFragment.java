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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import cat.jorda.traveltrack.R;
import cat.jorda.traveltrack.model.CustomMarker;
import cat.jorda.traveltrack.util.Constants;

/**
 * Created by xj1 on 22/08/2017.
 */

public class MapDayFragment extends Fragment implements OnMapReadyCallback, LocationListener
{
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 00001;
    private static String TAG = FinancesDayFragment.class.getSimpleName();

    private SupportMapFragment mSupportMapFragment;

    private GoogleMap map_;
    private IMapDayFragment delegate_;

    private String tripKey_;
    private String dayKey_;

    public interface IMapDayFragment
    {
        void onAddedMarker(Marker marker);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");

        tripKey_ = getArguments().getString(Constants.TRIP_KEY);
        dayKey_ = getArguments().getString(Constants.DAY_KEY);
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        if(context instanceof MapDayFragment.IMapDayFragment)  // context instanceof YourActivity
            this.delegate_ = (MapDayFragment.IMapDayFragment) context; // = (YourActivity) context
        else
            throw new ClassCastException(context.toString()
                    + " must implement StepsFragment.OnItemSelectedListener");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(cat.jorda.traveltrack.R.layout.map_details, container, false);

        mSupportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);

        if (mSupportMapFragment == null)
        {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mSupportMapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.mapView, mSupportMapFragment).commit();
        }

        if (mSupportMapFragment != null)
            mSupportMapFragment.getMapAsync(this);

        getCurrentLocation();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query postsQuery = getQuery(reference);

        postsQuery.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for (DataSnapshot markerSnapShot: dataSnapshot.getChildren())
                {
                    CustomMarker customMarker = markerSnapShot.getValue(CustomMarker.class);
                    Map<String, Object> message = (Map<String, Object>)markerSnapShot.getValue();
                    String dayID = (String) markerSnapShot.child("dayID").getValue();
                    String title = (String) markerSnapShot.child("title").getValue();
                    Log.d(TAG,"DayID: " + dayID + " title:" + title);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
            }
        });

        return rootView;
    }

    private boolean requestedMapPermission()
    {

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

    public Query getQuery(DatabaseReference databaseReference)
    {
        // All my days
        return databaseReference.child(Constants.DAY_MARKERS_TAB)
                .child(dayKey_);
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        if (googleMap != null)
        {
            map_ = googleMap;
            mapReadyLogic(map_);

            map_.setOnMapClickListener(latLng -> {
                Marker newMarker = map_.addMarker(new MarkerOptions().position(latLng).title("marker title").icon(BitmapDescriptorFactory.fromResource(R.drawable.map_markers)));
                delegate_.onAddedMarker(newMarker);
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
