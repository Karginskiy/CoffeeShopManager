package ru.nkargin.coffeeshopmanager.feature.start;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ru.nkargin.coffeeshopmanager.R;
import ru.nkargin.coffeeshopmanager.feature.admin.AdminActivity;
import ru.nkargin.coffeeshopmanager.feature.checkout.CheckoutActivity;
import ru.nkargin.coffeeshopmanager.feature.statistics.StatisticsActivity;
import ru.nkargin.coffeeshopmanager.model.StatisticTO;
import ru.nkargin.coffeeshopmanager.service.SessionService;
import ru.nkargin.coffeeshopmanager.service.StatisticsService;
import rx.functions.Action1;

public class StartActivity extends AppCompatActivity {

    private EditText ordersSumEditText;
    private TextView helloText;
    private Button checkoutButton;
    private Button adminButton;
    private EditText profitEditText;
    private EditText spendingEditText;
    private Button closeSessionButton;
    private Button statisticsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        bindFields();

        subscribeOnStatistics();
    }

    private void subscribeOnStatistics() {
        StatisticsService.INSTANCE
                .observeStatisticsForCurrentSession()
                .subscribe(subscribeOnCurrentSessionStatistics());
    }

    @NonNull
    private Action1<StatisticTO> subscribeOnCurrentSessionStatistics() {
        return new Action1<StatisticTO>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void call(StatisticTO statisticTO) {
                ordersSumEditText.setText(String.valueOf(statisticTO.getOrderSummary()) + " руб.");
                spendingEditText.setText(String.valueOf(statisticTO.getSpending()) + " руб.");
                profitEditText.setText(String.valueOf(statisticTO.getProfit()) + " руб.");
            }
        };
    }

    private void bindFields() {
        initButtons();
        initTotals();
    }

    private void initButtons() {
        checkoutButton = findViewById(R.id.checkout_button);
        checkoutButton.setOnClickListener(getCheckoutButtonClickListener());

        statisticsButton = findViewById(R.id.statistics_button);
        statisticsButton.setOnClickListener(getStatisticsButtonOnClickListener());

        closeSessionButton = findViewById(R.id.close_session_button);
        closeSessionButton.setOnClickListener(getCloseSessionButtonClickListener());

        adminButton = findViewById(R.id.admin_panel);
        adminButton.setOnClickListener(getOnAdminButtonClickListener());
    }

    private void initTotals() {
        spendingEditText = findViewById(R.id.spending_edit_text);
        ordersSumEditText = findViewById(R.id.orders_sum);
        profitEditText = findViewById(R.id.payment_text);
    }

    @NonNull
    private View.OnClickListener getStatisticsButtonOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartActivity.this, StatisticsActivity.class));
            }
        };
    }

    @NonNull
    private View.OnClickListener getCloseSessionButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(StartActivity.this)
                        .setTitle(R.string.confirm_session_close_alert_title)
                        .setNegativeButton(R.string.confirm_close_session_cancel_button, getOnAlertCancelClickListener())
                        .setPositiveButton(R.string.confirm_close_session_button, getOnAlertApproveClickListener()).show();
            }
        };
    }

    @NonNull
    private DialogInterface.OnClickListener getOnAlertCancelClickListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        };
    }

    @NonNull
    private DialogInterface.OnClickListener getOnAlertApproveClickListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SessionService.getInstance().closeCurrentSession();
                StartActivity.this.finish();
            }
        };
    }

    @NonNull
    private View.OnClickListener getCheckoutButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartActivity.this, CheckoutActivity.class));
            }
        };
    }

    @NonNull
    private View.OnClickListener getOnAdminButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartActivity.this, AdminActivity.class));
            }
        };
    }
}
