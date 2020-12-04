package ms.pradeep.covid_dashboard.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.List;

import javax.inject.Inject;

import ms.pradeep.covid_dashboard.R;
import ms.pradeep.covid_dashboard.models.ApiResponse;
import ms.pradeep.covid_dashboard.models.Attributes;
import ms.pradeep.covid_dashboard.models.CurrentLocation;
import ms.pradeep.covid_dashboard.models.Feature;
import ms.pradeep.covid_dashboard.utils.LocationSettingsUtil;
import ms.pradeep.covid_dashboard.utils.LocationUtils;
import ms.pradeep.covid_dashboard.viewmodels.DashboardActivityViewModel;

import static ms.pradeep.covid_dashboard.utils.Constants.REQUEST_LOCATION;
import static ms.pradeep.covid_dashboard.utils.Constants.REQUEST_LOCATION_SETTINGS;

/*
This is our only Activity, which hosts two Fragments via viewpager to display as cards.
 */
public class DashboardActivity extends BaseActivity<DashboardActivityViewModel> implements LocationUtils.OnLocationRequestedListener {
    private static final String TAG = "DashboardActivity";
    private static final int TOTAL_CARDS = 2;
    private ViewPager viewPager;

    @Inject
    DashboardActivityViewModel activityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabIndicator);
        tabLayout.setupWithViewPager(viewPager);
        checkLocationPermission();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public DashboardActivityViewModel getViewModel() {
        return activityViewModel;
    }

    // Request location permission.
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {// Has location permission.
            // Check if Location is turned on.
            new LocationSettingsUtil(this).turnLocationOn(new LocationSettingsUtil.LocationSettingsListener() {
                @Override
                public void locationSettingsStatus(boolean isLocationEnabled) {
                    if (isLocationEnabled) {
                        LocationUtils locationUtils = new LocationUtils(DashboardActivity.this);
                        locationUtils.connect();
                    }
                }
            });
        } else {
            // Directly ask for the permission.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && requestCode == REQUEST_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LocationUtils locationUtils = new LocationUtils(this);
                locationUtils.connect();
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(DashboardActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    //Show permission explanation dialog...
                } else {
                    //Never ask again selected, or device policy prohibits the app from having that permission.
                    //So, disable that feature, or fall back to another situation...
                }
                Toast.makeText(DashboardActivity.this, R.string.need_location, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOCATION_SETTINGS) {
            switch (resultCode) {
                case RESULT_OK: {
                    // All required changes were successfully made
                    LocationUtils locationUtils = new LocationUtils(this);
                    locationUtils.connect();
                    break;
                }
                case RESULT_CANCELED: {
                    // The user was asked to change settings, but chose not to
                    Toast.makeText(this, R.string.need_location, Toast.LENGTH_LONG).show();
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }

    @Override
    public void onLocationSuccess(CurrentLocation currentLocation) {
        Log.d(TAG, "onFindAddressSucceeded: " + currentLocation);
        callApiService(currentLocation);
    }

    @Override
    public void onLocationFailure() {
        Log.d(TAG, "onFindAddressFailure: ");
        Toast.makeText(this, R.string.invalid_location, Toast.LENGTH_LONG).show();
    }

    private void callApiService(final CurrentLocation currentLocation) {
        activityViewModel.getCovidStatsByCountry(currentLocation.getCountryIsoCode()).observe(this, new Observer<ApiResponse>() {
            @Override
            public void onChanged(ApiResponse apiResponse) {
                if (apiResponse == null) {
                    Toast.makeText(DashboardActivity.this, R.string.generic_error_message, Toast.LENGTH_LONG).show();
                    return;
                }
                List<Feature> features = apiResponse.getFeatures();
                try {
                    Attributes attributes;
                    if (features.size() > 0) {
                        attributes = features.get(0).getAttributes();
                    } else {
                        // No Covid-19 cases found in this country? Show the result
                        // Ex: country code - sj (Svalbard and Jan Mayen Islands)
                        attributes = new Attributes(0, 0, -1);
                    }
                    setUpFragments(attributes, currentLocation);
                } catch (NullPointerException e) {
                    Toast.makeText(DashboardActivity.this, R.string.generic_error_message, Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    // Setup viewpager adapters to show covid stats
    private void setUpFragments(final Attributes attributes, final CurrentLocation currentLocation) {
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    // Swipe right card
                    return PilotFragment.newInstance();
                } else {
                    //Covid stats card
                    return DashboardFragment.newInstance(attributes, currentLocation);
                }
            }

            @Override
            public int getCount() {
                return TOTAL_CARDS;
            }
        });
    }
}