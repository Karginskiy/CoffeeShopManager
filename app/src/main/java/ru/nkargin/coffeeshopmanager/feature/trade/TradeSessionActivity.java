package ru.nkargin.coffeeshopmanager.feature.trade;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ru.nkargin.coffeeshopmanager.R;
import ru.nkargin.coffeeshopmanager.feature.checkout.CheckoutActivity;
import ru.nkargin.coffeeshopmanager.service.OrderService;
import ru.nkargin.coffeeshopmanager.service.SessionService;

public class TradeSessionActivity extends AppCompatActivity {

    private Button checkoutButton;
    private Button closeSessionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trade_session_activity);

        RecyclerView closedOrdersRecycler = findViewById(R.id.sold_orders_recycler);
        closedOrdersRecycler.setLayoutManager(new GridLayoutManager(this, 1));
        closedOrdersRecycler.setAdapter(new TradeSessionClosedOrdersAdapter(this));

        bindFields();
    }


    private void bindFields() {
        initButtons();
    }

    private void initButtons() {
        checkoutButton = findViewById(R.id.checkout_button);
        checkoutButton.setOnClickListener(getCheckoutButtonClickListener());

        closeSessionButton = findViewById(R.id.close_session_button);
        closeSessionButton.setOnClickListener(getCloseSessionButtonClickListener());
    }


    @NonNull
    private View.OnClickListener getCloseSessionButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (OrderService.INSTANCE.getCurrentOrder() == null) {
                    new AlertDialog.Builder(TradeSessionActivity.this)
                            .setTitle(R.string.confirm_session_close_alert_title)
                            .setMessage(R.string.close_session_warning)
                            .setNegativeButton(R.string.confirm_close_session_cancel_button, getOnAlertCancelClickListener())
                            .setPositiveButton(R.string.confirm_close_session_button, getOnAlertApproveClickListener()).show();

                } else {
                    new AlertDialog.Builder(TradeSessionActivity.this)
                            .setMessage(R.string.not_closed_order_title)
                            .setTitle(R.string.error_title)
                            .setPositiveButton(R.string.confirm_label, getOnAlertCancelClickListener())
                            .show();
                }
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
                TradeSessionActivity.this.finish();
            }
        };
    }

    @NonNull
    private View.OnClickListener getCheckoutButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TradeSessionActivity.this, CheckoutActivity.class));
            }
        };
    }

}
