package ms.pradeep.covid_dashboard.di;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ms.pradeep.covid_dashboard.BuildConfig;
import ms.pradeep.covid_dashboard.networking.RestApi;
import ms.pradeep.covid_dashboard.utils.Constants;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logging);
        }
        httpClient.connectTimeout(600, TimeUnit.SECONDS);
        httpClient.writeTimeout(120, TimeUnit.SECONDS);
        httpClient.readTimeout(600, TimeUnit.SECONDS);
        return httpClient.build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    RestApi provideRestApi(Retrofit retrofit) {
        return retrofit.create(RestApi.class);
    }
}
