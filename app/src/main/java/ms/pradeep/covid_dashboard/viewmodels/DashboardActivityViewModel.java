package ms.pradeep.covid_dashboard.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.inject.Inject;

import ms.pradeep.covid_dashboard.models.ApiResponse;
import ms.pradeep.covid_dashboard.repositories.DashboardRepository;

public class DashboardActivityViewModel extends ViewModel {
    private MutableLiveData<ApiResponse> mutableLiveData;
    private DashboardRepository dashboardRepository;

    @Inject
    public DashboardActivityViewModel(DashboardRepository repository) {
        this.dashboardRepository = repository;
    }

    public LiveData<ApiResponse> getCovidStatsByCountry(String countryIsoCode) {
        if (mutableLiveData == null) {
            mutableLiveData = dashboardRepository.getCovidStatsByCountry(countryIsoCode);
        }
        return mutableLiveData;
    }
}
