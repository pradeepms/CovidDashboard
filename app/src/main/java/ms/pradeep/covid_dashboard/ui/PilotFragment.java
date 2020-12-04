package ms.pradeep.covid_dashboard.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ms.pradeep.covid_dashboard.R;
/*
Card 1 - Which show
 */
public class PilotFragment extends Fragment {
    private static final String TAG = "PilotFragment";

    public static PilotFragment newInstance(){
        return new PilotFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pilot, null);
    }
}
