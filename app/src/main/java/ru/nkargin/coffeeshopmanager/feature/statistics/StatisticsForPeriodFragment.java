package ru.nkargin.coffeeshopmanager.feature.statistics;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import ru.nkargin.coffeeshopmanager.R;
import ru.nkargin.coffeeshopmanager.model.StatisticTO;
import ru.nkargin.coffeeshopmanager.service.StatisticsService;

public class StatisticsForPeriodFragment extends Fragment implements DatePickerDialog.OnDateSetListener{

    private static final StatisticsForPeriodFragment INSTANCE = new StatisticsForPeriodFragment();
    private static final String DATE_FROM = "dateFrom";
    private static final String DATE_TO = "dateTo";
    private static final SimpleDateFormat standardFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    private static final String STATISTICS = "statistics";

    private EditText dateFrom;
    private EditText dateTo;
    private EditText ordersSum;
    private EditText spending;
    private EditText profit;
    private DatePickerDialog dpd;
    private Disposable onPeriodSubscription;

    public StatisticsForPeriodFragment() {}

    public static StatisticsForPeriodFragment getInstance() {
        return INSTANCE;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_statistics_for_period, container, false);
        bindFields(inflate);

        Pair<Calendar, Calendar> datesFromPreferences = getDatesFromPreferences();

        resubscribeOnPeriodStatistics(datesFromPreferences);
        updateDateFields(datesFromPreferences);

        return inflate;
    }

    private void bindFields(View inflate) {
        Button chooseDatesButton = inflate.findViewById(R.id.choose_dates_button);
        chooseDatesButton.setOnClickListener(addOnChooseDatesClickButton());
        initDateFields(inflate);
        ordersSum = inflate.findViewById(R.id.orders_sum);
        spending = inflate.findViewById(R.id.spending);
        profit = inflate.findViewById(R.id.profit);
        initDatePicker();
    }

    private void initDateFields(View inflate) {
        dateFrom = inflate.findViewById(R.id.date_from);
        dateFrom.setOnClickListener(getDateFromClickListener());

        dateTo = inflate.findViewById(R.id.date_to);
        dateTo.setOnClickListener(getDateToClickListener());
    }

    @NonNull
    private View.OnClickListener getDateFromClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSeparateDatepickerDialog(DATE_FROM, dateFrom);
            }
        };
    }

    @NonNull
    private View.OnClickListener getDateToClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSeparateDatepickerDialog(DATE_TO, dateTo);
            }
        };
    }

    private void initDatePicker() {
        Pair<Calendar, Calendar> savedDates = getDatesFromPreferences();
        dpd = DatePickerDialog.newInstance(
                StatisticsForPeriodFragment.this,
                savedDates.first.get(Calendar.YEAR),
                savedDates.first.get(Calendar.MONTH),
                savedDates.first.get(Calendar.DAY_OF_MONTH),
                savedDates.second.get(Calendar.YEAR),
                savedDates.second.get(Calendar.MONTH),
                savedDates.second.get(Calendar.DAY_OF_MONTH)
        );
        updateTitles();
    }

    private void updateTitles() {
        dpd.setStartTitle(getString(R.string.date_picker_from));
        dpd.setEndTitle(getString(R.string.date_picker_to));
    }

    @Override
    public void onResume() {
        super.onResume();
        resubscribeOnPeriodStatistics(getDatesFromPreferences());
        updateTitles();
    }

    private void updateDateFields(Pair<Calendar, Calendar> datesFromPreferences) {
        dateFrom.setText(standardFormat.format(datesFromPreferences.first.getTime()));
        dateTo.setText(standardFormat.format(datesFromPreferences.second.getTime()));
    }

    private void resubscribeOnPeriodStatistics(Pair<Calendar, Calendar> datesFromPreferences) {
        if (onPeriodSubscription != null) {
            onPeriodSubscription.dispose();
        }
        onPeriodSubscription = StatisticsService.INSTANCE
                .observeStatisticsForDatesBetween(datesFromPreferences)
                .subscribe(subscribeOnPeriodStatistics());
    }

    @NonNull
    private Consumer<StatisticTO> subscribeOnPeriodStatistics() {
        return new Consumer<StatisticTO>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void accept(StatisticTO statisticTO) {
                ordersSum.setText(statisticTO.getOrderSummary() + " руб.");
                spending.setText(statisticTO.getSpending() + " руб.");
                profit.setText(statisticTO.getProfit() + " руб.");
            }
        };
    }

    @NonNull
    private View.OnClickListener addOnChooseDatesClickButton() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        };
    }

    private void showSeparateDatepickerDialog(String fieldName, EditText dateField) {
        Calendar dateFromPreferences = getDateFromPreferences(fieldName);
        android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(getContext(),
                R.style.datepicker,
                getOnDateSetSeparatePicker(fieldName, dateField),
                dateFromPreferences.get(Calendar.YEAR),
                dateFromPreferences.get(Calendar.MONTH),
                dateFromPreferences.get(Calendar.DATE)
        );

        datePickerDialog.show();
    }

    @NonNull
    private android.app.DatePickerDialog.OnDateSetListener getOnDateSetSeparatePicker(final String fieldName, final EditText dateField) {
        return new android.app.DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfYear) {
                Calendar dateFrom = Calendar.getInstance(Locale.getDefault());
                dateFrom.set(year, monthOfYear, dayOfYear);
                setDatesToPreferences(dateFrom, fieldName);

                dateField.setText(standardFormat.format(dateFrom.getTime()));
                resubscribeOnPeriodStatistics(getDatesFromPreferences());
            }
        };
    }

    private void showDatePickerDialog() {
        dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        Calendar dateFrom = Calendar.getInstance(Locale.getDefault());
        dateFrom.set(year, monthOfYear, dayOfMonth);
        setDatesToPreferences(dateFrom, DATE_FROM);
        this.dateFrom.setText(standardFormat.format(dateFrom.getTime()));

        Calendar dateTo = Calendar.getInstance(Locale.getDefault());
        dateTo.set(yearEnd, monthOfYearEnd, dayOfMonthEnd);
        setDatesToPreferences(dateTo, DATE_TO);
        this.dateTo.setText(standardFormat.format(dateTo.getTime()));

        resubscribeOnPeriodStatistics(Pair.create(dateFrom, dateTo));
    }

    private Pair<Calendar, Calendar> getDatesFromPreferences() {
        Calendar calendarFrom = getDateFromPreferences(DATE_FROM);
        Calendar calendarTo = getDateFromPreferences(DATE_TO);

        return Pair.create(calendarFrom, calendarTo);
    }

    @NonNull
    private Calendar getDateFromPreferences(String fieldName) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(STATISTICS, Context.MODE_PRIVATE);
        long dateFrom = sharedPreferences.getLong(fieldName, new Date().getTime());

        Calendar calendarFrom = Calendar.getInstance(Locale.getDefault());
        calendarFrom.setTimeInMillis(dateFrom);
        return calendarFrom;
    }

    private void setDatesToPreferences(Calendar calendar, String name) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(STATISTICS, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putLong(name, calendar.getTimeInMillis());
        edit.apply();
    }
}
