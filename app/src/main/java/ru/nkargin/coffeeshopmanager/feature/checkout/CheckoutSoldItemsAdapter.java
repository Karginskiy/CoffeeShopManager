package ru.nkargin.coffeeshopmanager.feature.checkout;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;
import ru.nkargin.coffeeshopmanager.R;
import ru.nkargin.coffeeshopmanager.databinding.SoldListItemBinding;
import ru.nkargin.coffeeshopmanager.model.Good;
import ru.nkargin.coffeeshopmanager.model.IncompleteShopOrder;


class CheckoutSoldItemsAdapter extends RecyclerView.Adapter<CheckoutSoldItemHolder> {

    private Activity activity;
    private IncompleteShopOrder currentOrder;
    private List<Map.Entry<Good, Integer>> soldItems = new ArrayList<>();

    public CheckoutSoldItemsAdapter(Activity activity, IncompleteShopOrder currentOrder) {
        this.activity = activity;
        this.currentOrder = currentOrder;

        currentOrder.observeGoods().subscribe(goodsConsumer());
    }

    @NonNull
    private Consumer<HashMap<Good, Integer>> goodsConsumer() {
        return new Consumer<HashMap<Good,Integer>>() {
            @Override
            public void accept(HashMap<Good, Integer> goodIntegerMap) {
                soldItems = new ArrayList<>(goodIntegerMap.entrySet());
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public CheckoutSoldItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        SoldListItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.sold_list_item, parent, false);
        return new CheckoutSoldItemHolder(binding);
    }

    @Override
    public void onBindViewHolder(CheckoutSoldItemHolder holder, int position) {
        holder.bind(soldItems.get(position));
    }

    @Override
    public int getItemCount() {
        return soldItems.size();
    }
}
