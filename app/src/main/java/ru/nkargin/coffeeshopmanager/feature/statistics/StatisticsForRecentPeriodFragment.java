package ru.nkargin.coffeeshopmanager.feature.statistics;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


import java.util.Calendar;
import java.util.Locale;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import ru.nkargin.coffeeshopmanager.R;
import ru.nkargin.coffeeshopmanager.model.StatisticTO;
import ru.nkargin.coffeeshopmanager.service.StatisticsService;

public class StatisticsForRecentPeriodFragment extends Fragment {

    private static StatisticsForRecentPeriodFragment INSTANCE;
    private EditText spendingWeek;
    private Disposable weeklySubscription;
    private EditText profitWeek;
    private Disposable monthlySubscription;
    private Disposable yearlySubscription;
    private EditText spendingMonth;
    private EditText profitMonth;
    private EditText spendingYear;
    private EditText profitYear;

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
        View inflate = inflater.inflate(R.layout.fragment_statistics_for_recent_period, container, false);
        spendingWeek = inflate.findViewById(R.id.spending_week);
        profitWeek = inflate.findViewById(R.id.profit_week);

        spendingMonth = inflate.findViewById(R.id.spending_month);
        profitMonth = inflate.findViewById(R.id.profit_month);

        spendingYear = inflate.findViewById(R.id.spending_year);
        profitYear = inflate.findViewById(R.id.profit_year);

        resubscribeOnStatistics();

        return inflate;
    }

    private void resubscribeOnStatistics() {
        subscribeOnWeekSummariesUpdate();
        subscribeOnMonthSummariesUpdate();
        subscribeOnYearSummariesUpdate();
    }

    @Override
    public void onResume() {
        super.onResume();
        resubscribeOnStatistics();
    }

    private void subscribeOnWeekSummariesUpdate() {
        Calendar from = Calendar.getInstance(Locale.getDefault());
        Calendar to = Calendar.getInstance(Locale.getDefault());
        setWeekFirstDay(from);
        setDayOnStart(from);

        if (weeklySubscription != null) {
            weeklySubscription.dispose();
        }

        weeklySubscription = StatisticsService
                .INSTANCE
                .observeStatisticsForDatesBetween(Pair.create(from, to))
                .subscribe(updateViewOnWeekDataUpdate());
    }

    private void subscribeOnMonthSummariesUpdate() {
        Calendar from = Calendar.getInstance(Locale.getDefault());
        Calendar to = Calendar.getInstance(Locale.getDefault());
        setMonthFirstDay(from);
        setDayOnStart(from);

        if (monthlySubscription != null) {
            monthlySubscription.dispose();
        }

        monthlySubscription = StatisticsService
                .INSTANCE
                .observeStatisticsForDatesBetween(Pair.create(from, to))
                .subscribe(updateViewOnMonthDataUpdate());
    }

    @NonNull
    private Consumer<StatisticTO> updateViewOnMonthDataUpdate() {
        return new Consumer<StatisticTO>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void accept(StatisticTO statisticTO) {
                spendingMonth.setText(String.valueOf(statisticTO.getSpending()) + " руб.");
                profitMonth.setText(String.valueOf(statisticTO.getProfit())+ " руб.");
            }
        };
    }

    private void subscribeOnYearSummariesUpdate() {
        Calendar from = Calendar.getInstance(Locale.getDefault());
        Calendar to = Calendar.getInstance(Locale.getDefault());
        setYearFirstDay(from);
        setDayOnStart(from);

        if (yearlySubscription != null) {
            yearlySubscription.dispose();
        }

        yearlySubscription = StatisticsService
                .INSTANCE
                .observeStatisticsForDatesBetween(Pair.create(from, to))
                .subscribe(updateViewOnYearlyDataChange());
    }

    @NonNull
    private Consumer<StatisticTO> updateViewOnYearlyDataChange() {
        return new Consumer<StatisticTO>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void accept(StatisticTO statisticTO) {
                spendingYear.setText(String.valueOf(statisticTO.getSpending()) + " руб.");
                profitYear.setText(String.valueOf(statisticTO.getProfit())+ " руб.");
            }
        };
    }


    @NonNull
    private Consumer<StatisticTO> updateViewOnWeekDataUpdate() {
        return new Consumer<StatisticTO>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void accept(StatisticTO statisticTO) {
                spendingWeek.setText(String.valueOf(statisticTO.getSpending()) + " руб.");
                profitWeek.setText(String.valueOf(statisticTO.getProfit())+ " руб.");
            }
        };
    }

    private void setYearFirstDay(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMinimum(Calendar.DAY_OF_YEAR));
    }

    private void setWeekFirstDay(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getActualMinimum(Calendar.DAY_OF_WEEK));
    }

    private void setMonthFirstDay(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
    }

    private void setDayOnStart(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
    }
}
