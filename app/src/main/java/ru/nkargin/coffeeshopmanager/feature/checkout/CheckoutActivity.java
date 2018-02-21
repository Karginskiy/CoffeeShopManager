package ru.nkargin.coffeeshopmanager.feature.checkout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import ru.nkargin.coffeeshopmanager.R;
import ru.nkargin.coffeeshopmanager.model.IncompleteShopOrder;
import ru.nkargin.coffeeshopmanager.service.OrderService;

public class CheckoutActivity extends AppCompatActivity {

    private IncompleteShopOrder currentOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        RecyclerView orderGoodsRecycler = findViewById(R.id.order_goods_recycler);
        orderGoodsRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        orderGoodsRecycler.setAdapter(new CheckoutGoodListAdapter(this));

        currentOrder = OrderService.INSTANCE.startOrder();
    }

}
