package ms.pradeep.covid_dashboard;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import ms.pradeep.covid_dashboard.di.DaggerApplicationComponent;

public class CovidDashboardApplication extends DaggerApplication {

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerApplicationComponent.builder().create(this);
    }
}
