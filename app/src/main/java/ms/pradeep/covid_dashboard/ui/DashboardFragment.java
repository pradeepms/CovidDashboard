package ms.pradeep.covid_dashboard.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.NumberFormat;
import java.util.Locale;

import ms.pradeep.covid_dashboard.R;
import ms.pradeep.covid_dashboard.models.Attributes;
import ms.pradeep.covid_dashboard.models.CurrentLocation;

public class DashboardFragment extends Fragment {
    private static final String TAG = "DashboardFragment";
    private static final String ARG_ATTRIBUTES = "ARG_ATTRIBUTES";
    private static final String ARG_LOCATION = "ARG_LOCATION";
    private Attributes attributes;
    private CurrentLocation currentLocation;

    public static DashboardFragment newInstance(Attributes attributes, CurrentLocation currentLocation) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_ATTRIBUTES, attributes);
        args.putParcelable(ARG_LOCATION, currentLocation);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, null);
        TextView tvCountrySituation = view.findViewById(R.id.tv_country_situation);
        TextView tvConfirmedCases = view.findViewById(R.id.tv_confirmed_count);
        TextView tvDeathCount = view.findViewById(R.id.tv_death_count);
        tvConfirmedCases.setText(getFormattedNumber(attributes.getCumCase()));
        String totalsDeaths = getString(R.string.total_deaths, getFormattedNumber(attributes.getCumDeath()));
        tvDeathCount.setText(totalsDeaths);
        tvCountrySituation.setText(getString(R.string.country_situation, currentLocation.getCountryName()));
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            attributes = getArguments().getParcelable(ARG_ATTRIBUTES);
            currentLocation = getArguments().getParcelable(ARG_LOCATION);
        }
    }

    private String getFormattedNumber(int number) {
        return NumberFormat.getInstance(Locale.getDefault()).format(number);
    }
}
