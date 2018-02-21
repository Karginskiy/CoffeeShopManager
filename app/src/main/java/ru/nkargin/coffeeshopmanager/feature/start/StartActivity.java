package ru.nkargin.coffeeshopmanager.feature.start;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ru.nkargin.coffeeshopmanager.R;
import ru.nkargin.coffeeshopmanager.feature.admin.AdminActivity;
import ru.nkargin.coffeeshopmanager.feature.checkout.CheckoutActivity;
import ru.nkargin.coffeeshopmanager.service.ConsumablesService;
import ru.nkargin.coffeeshopmanager.service.SessionService;
import ru.nkargin.coffeeshopmanager.service.StatisticsService;
import rx.Subscription;
import rx.functions.Action1;

public class StartActivity extends AppCompatActivity {

    private EditText ordersSumEditText;
    private TextView helloText;
    private Button checkoutButton;
    private Button adminButton;
    private EditText paymentEditText;
    private EditText spendingEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        bindFields();

        subscribeOnOrdersSummary();
        subscribeOnPayment();
        subscribeOnSpending();
    }

    private void subscribeOnSpending() {
        StatisticsService.INSTANCE.observeSpendingForCurrentSession().subscribe(new Action1<Integer>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void call(Integer integer) {
                spendingEditText.setText(String.valueOf(integer) + " руб.");
            }
        });
    }

    private void subscribeOnPayment() {
        StatisticsService.INSTANCE.observePaymentForCurrentSession().subscribe(new Action1<Integer>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void call(Integer integer) {
                paymentEditText.setText(String.valueOf(integer) + " руб.");
            }
        });
    }

    private void subscribeOnOrdersSummary() {
        StatisticsService.INSTANCE.observeOrdersSummaryForCurrentSession().subscribe(new Action1<Integer>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void call(Integer integer) {
                ordersSumEditText.setText(String.valueOf(integer) + " руб.");
            }
        });
    }

    private void bindFields() {
        checkoutButton = findViewById(R.id.checkout_button);
        checkoutButton.setOnClickListener(getCheckoutButtonClickListener());

        spendingEditText = findViewById(R.id.spending_edit_text);
        ordersSumEditText = findViewById(R.id.orders_sum);
        paymentEditText = findViewById(R.id.payment_text);
        helloText = findViewById(R.id.hello_message);
        helloText.setText(String.format("Привет, %s!", SessionService.getInstance().getCurrentUser().getRealName()));

        adminButton = findViewById(R.id.admin_panel);
        adminButton.setOnClickListener(getOnAdminButtonClickListener());
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
