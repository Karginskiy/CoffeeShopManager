package ru.nkargin.coffeeshopmanager.feature.checkout;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import ru.nkargin.coffeeshopmanager.R;
import ru.nkargin.coffeeshopmanager.databinding.ActivityCheckoutBinding;
import ru.nkargin.coffeeshopmanager.service.OrderService;

import static ru.nkargin.coffeeshopmanager.feature.checkout.CheckoutRxUtils.getOnTotalsHandler;

public class CheckoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityCheckoutBinding checkoutBinding = DataBindingUtil.setContentView(this, R.layout.activity_checkout);
        checkoutBinding.setViewModel(new CheckoutTotalsViewModel());

        addOrderButtonsClickListeners(checkoutBinding);
        initOrder(checkoutBinding);
        initGoodsRecycler();
        initSoldItemsRecycler();
    }

    private void addOrderButtonsClickListeners(ActivityCheckoutBinding checkoutBinding) {
        addApproveButtonClickListener(checkoutBinding);
        addCancelButtonClickListener(checkoutBinding);
    }

    private void addCancelButtonClickListener(ActivityCheckoutBinding checkoutBinding) {
        checkoutBinding.cancelOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderService.INSTANCE.cancelOrder();
                finish();
            }
        });
    }

    private void addApproveButtonClickListener(ActivityCheckoutBinding checkoutBinding) {
        checkoutBinding.approveOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderService.INSTANCE.approveAndCloseOrder();
                finish();
            }
        });
    }

    private void initOrder(ActivityCheckoutBinding checkoutBinding) {
        OrderService.INSTANCE
                .startOrderOrGetOpened()
                .observeSummary()
                .subscribe(getOnTotalsHandler(checkoutBinding));
    }

    private void initSoldItemsRecycler() {
        RecyclerView soldItemsRecycler = findViewById(R.id.sold_items_recycler);
        soldItemsRecycler.setLayoutManager(new GridLayoutManager(this, 1));
        soldItemsRecycler.setAdapter(new CheckoutSoldItemsAdapter(this, OrderService.INSTANCE.getCurrentOrder()));
    }

    private void initGoodsRecycler() {
        RecyclerView orderGoodsRecycler = findViewById(R.id.order_goods_recycler);
        orderGoodsRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        orderGoodsRecycler.setAdapter(new CheckoutGoodListAdapter(this));
    }

}
