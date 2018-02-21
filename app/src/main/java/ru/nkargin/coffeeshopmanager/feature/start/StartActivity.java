package ru.nkargin.coffeeshopmanager.feature.start;

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
import rx.functions.Action1;

public class StartActivity extends AppCompatActivity {

    private EditText ordersSumEditText;
    private EditText cupsLeft;
    private TextView helloText;
    private Button checkoutButton;
    private Button adminButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        bindFields();

        subscribeOnCups();
    }

    private void bindFields() {
        checkoutButton = findViewById(R.id.checkout_button);
        checkoutButton.setOnClickListener(getCheckoutButtonClickListener());

        ordersSumEditText = findViewById(R.id.orders_sum);
        cupsLeft = findViewById(R.id.cups_left);
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

    private void subscribeOnCups() {
        ConsumablesService.INSTANCE.observeCupsCount().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                cupsLeft.setText(String.valueOf(integer));
            }
        });
    }
}
