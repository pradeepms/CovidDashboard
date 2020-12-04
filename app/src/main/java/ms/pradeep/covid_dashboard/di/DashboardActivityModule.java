package ms.pradeep.covid_dashboard.di;

import dagger.Module;
import dagger.Provides;
import ms.pradeep.covid_dashboard.networking.RestApi;
import ms.pradeep.covid_dashboard.repositories.DashboardRepository;
import ms.pradeep.covid_dashboard.viewmodels.DashboardActivityViewModel;

@Module
public class DashboardActivityModule {
    @Provides
    DashboardRepository provideApiRepository(RestApi restApi){
        return new DashboardRepository(restApi);
    }
    @Provides
    DashboardActivityViewModel provideDashboardActivityModel(DashboardRepository repository) {
        return new DashboardActivityViewModel(repository);
    }
}
