package ms.pradeep.covid_dashboard.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import ms.pradeep.covid_dashboard.ui.DashboardActivity;

@Module
public abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = {DashboardActivityModule.class})
    abstract DashboardActivity contributeDashboardActivity();
}
