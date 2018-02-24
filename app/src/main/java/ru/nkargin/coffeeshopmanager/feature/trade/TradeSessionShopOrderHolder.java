package ru.nkargin.coffeeshopmanager.feature.trade;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import ru.nkargin.coffeeshopmanager.databinding.OrderBinding;
import ru.nkargin.coffeeshopmanager.model.ShopOrder;

/**
 * Created by hei on 24.02.2018.
 */

class TradeSessionShopOrderHolder extends RecyclerView.ViewHolder {
    private final OrderBinding binding;

    public TradeSessionShopOrderHolder(OrderBinding binding) {
        super(binding.getRoot());

        this.binding = binding;
    }

    public void bind(ShopOrder shopOrder) {
        binding.setViewModel(new TradeSessionClosedOrderViewModel());

        binding.getViewModel().setShopOrder(shopOrder);
    }
}
