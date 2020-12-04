package ms.pradeep.covid_dashboard.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import ms.pradeep.covid_dashboard.models.CurrentLocation;

/*
This Util class is responsible for getting last known location of the user.
Calling class is required to implement the interface OnLocationRequestedListener.
Processed location information is sent in interface callbacks.
 */

public class LocationUtils implements OnSuccessListener<Location>, OnFailureListener {
    private static final String TAG = "LocationUtils";
    private Context mContext;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;

    public LocationUtils(Context context) {
        mContext = context;
        if (context instanceof OnLocationRequestedListener) {// Must implement the interface
            buildApiClient();
        } else {
            throw new IllegalStateException(mContext.getClass().getSimpleName() + " must implement OnLocationRequestedListener");
        }
    }

    private void buildApiClient() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext);
    }

    @SuppressLint("MissingPermission")
    public void connect() {
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this).addOnFailureListener(this);
    }

    public void disconnect() {

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onSuccess(Location location) {
        if (location == null) {// If location is null, request for 1 time location updates.
            Log.e(TAG, "onSuccess: getLastLocation is null, requesting for location updates");
            LocationRequest mLocationRequest = new LocationRequest();
            mLocationRequest.setNumUpdates(1);
            mLocationRequest.setInterval(0);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        Log.e(TAG, "onLocationResult: null");
                        return;
                    }
                    for (Location location : locationResult.getLocations()) {
                        if (location != null) {
                            getAddress(location);
                            if (fusedLocationProviderClient != null) {
                                fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                            }
                        }
                    }
                }
            };
            fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, locationCallback, null);
        } else {
            getAddress(location);
        }
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        Log.e(TAG, "onFailure: ", e);
        ((OnLocationRequestedListener) mContext).onLocationFailure();
    }

    // Geocoder to get locat
    private void getAddress(Location location) {
        Geocoder geocoder = new Geocoder(mContext, Locale.US);
        try {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (!addresses.isEmpty()) {
                CurrentLocation currentLocation = fromAddress(addresses.get(0));
                if (currentLocation == null) {
                    ((OnLocationRequestedListener) mContext).onLocationFailure();
                } else {
                    ((OnLocationRequestedListener) mContext).onLocationSuccess(currentLocation);
                }
            } else {
                ((OnLocationRequestedListener) mContext).onLocationFailure();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "getAddress: Failed", e);
            ((OnLocationRequestedListener) mContext).onLocationFailure();
        }
    }

    private CurrentLocation fromAddress(Address address) {
        Log.d(TAG, "fromAddress: " + address);
        if (address.getCountryCode() == null) {
            return null;
        }
        return new CurrentLocation(address.getLatitude(), address.getLongitude(), address.getAddressLine(0),
                address.getCountryCode(), address.getCountryName());
    }

    public interface OnLocationRequestedListener {
        void onLocationSuccess(CurrentLocation currentLocation);

        void onLocationFailure();
    }
}
