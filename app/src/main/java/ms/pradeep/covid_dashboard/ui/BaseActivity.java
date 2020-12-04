package ms.pradeep.covid_dashboard.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import dagger.android.support.DaggerAppCompatActivity;

public abstract class BaseActivity<T extends ViewModel> extends DaggerAppCompatActivity {
    protected abstract int getLayout();

    private T viewModel;

    public abstract T getViewModel();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        this.viewModel = viewModel == null ? getViewModel() : viewModel;
    }
}
