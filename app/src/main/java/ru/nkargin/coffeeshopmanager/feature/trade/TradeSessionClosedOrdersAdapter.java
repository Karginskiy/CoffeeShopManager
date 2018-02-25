package ru.nkargin.coffeeshopmanager.feature.trade;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;
import ru.nkargin.coffeeshopmanager.R;
import ru.nkargin.coffeeshopmanager.databinding.OrderBinding;
import ru.nkargin.coffeeshopmanager.model.ShopOrder;
import ru.nkargin.coffeeshopmanager.service.OrderService;


class TradeSessionClosedOrdersAdapter extends RecyclerView.Adapter<TradeSessionShopOrderHolder> {

    private Activity activity;
    private List<ShopOrder> shopOrders = new ArrayList<>();

    TradeSessionClosedOrdersAdapter(Activity activity) {
        this.activity = activity;

        subscribeOnOrdersForCurrentSession();
    }

    private void subscribeOnOrdersForCurrentSession() {
        OrderService.INSTANCE.observeShopOrdersForCurrentSession().subscribe(onShopOrdersChanged());
    }

    @NonNull
    private Consumer<List<ShopOrder>> onShopOrdersChanged() {
        return new Consumer<List<ShopOrder>>() {
            @Override
            public void accept(List<ShopOrder> shopOrders) throws Exception {
                TradeSessionClosedOrdersAdapter.this.shopOrders = shopOrders;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public TradeSessionShopOrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        OrderBinding binding = DataBindingUtil.inflate(inflater, R.layout.order, parent, false);
        return new TradeSessionShopOrderHolder(binding);
    }

    @Override
    public void onBindViewHolder(TradeSessionShopOrderHolder holder, int position) {
        holder.bind(shopOrders.get(position));
    }

    @Override
    public int getItemCount() {
        return shopOrders.size();
    }
}
