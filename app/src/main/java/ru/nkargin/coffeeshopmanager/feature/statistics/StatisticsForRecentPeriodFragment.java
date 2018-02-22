package ru.nkargin.coffeeshopmanager.feature.statistics;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.nkargin.coffeeshopmanager.R;

public class StatisticsForRecentPeriodFragment extends Fragment {

    private static StatisticsForRecentPeriodFragment INSTANCE;

    public StatisticsForRecentPeriodFragment() {}

    public static StatisticsForRecentPeriodFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new StatisticsForRecentPeriodFragment();
        }
        return INSTANCE;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statistics_for_recent_period, container, false);
    }
}
