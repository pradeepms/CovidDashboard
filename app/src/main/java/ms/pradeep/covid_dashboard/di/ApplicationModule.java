package ms.pradeep.covid_dashboard.di;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    @Provides
    @Singleton
    Context providesApplication(Application application) {
        return application;
    }
}
