package ms.pradeep.covid_dashboard.di;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import ms.pradeep.covid_dashboard.CovidDashboardApplication;

@Singleton
@Component(modules = {AndroidInjectionModule.class, ApplicationModule.class, NetworkModule.class, ActivityBuilder.class})
public interface ApplicationComponent extends AndroidInjector<CovidDashboardApplication> {
    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<CovidDashboardApplication> {
    }
}
