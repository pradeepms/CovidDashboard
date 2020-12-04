package ms.pradeep.covid_dashboard.repositories;

import androidx.lifecycle.MutableLiveData;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.inject.Inject;

import ms.pradeep.covid_dashboard.models.ApiResponse;
import ms.pradeep.covid_dashboard.networking.RestApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/*
* This repository is responsible for providing data to the UI/View through ViewModel using LiveData.
* Data will be fetched from the Network API data source.
*
* This repository can be expanded to support multiple data source, for example Cache.
*/
public class DashboardRepository {
    private RestApi restApi;

    @Inject
    public DashboardRepository(RestApi restApi) {
        this.restApi = restApi;
    }

    public MutableLiveData<ApiResponse> getCovidStatsByCountry(String countryIsoCode) {
        final MutableLiveData<ApiResponse> mutableLiveData = new MutableLiveData<>();
        String formattedCountryIsoQuery = String.format("ISO_2_CODE='%s'", countryIsoCode);
        try {
            String encodedCountryIsoQuery = URLEncoder.encode(formattedCountryIsoQuery, "utf-8");
            restApi.getApiResponse(encodedCountryIsoQuery).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        mutableLiveData.setValue(response.body());
                    } else {
                        mutableLiveData.setValue(null);
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    mutableLiveData.setValue(null);
                }
            });
        } catch (UnsupportedEncodingException e) {
            mutableLiveData.setValue(null);
        }

        return mutableLiveData;
    }
}
