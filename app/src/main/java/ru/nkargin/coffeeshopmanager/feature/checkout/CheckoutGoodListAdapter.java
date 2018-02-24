package ru.nkargin.coffeeshopmanager.feature.checkout;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.functions.Consumer;
import ru.nkargin.coffeeshopmanager.R;
import ru.nkargin.coffeeshopmanager.databinding.GoodBinding;
import ru.nkargin.coffeeshopmanager.model.Good;
import ru.nkargin.coffeeshopmanager.service.GoodService;


public class CheckoutGoodListAdapter extends RecyclerView.Adapter<CheckoutGoodHolder> {

    private final Activity activity;
    private List<Good> goodList;

    public CheckoutGoodListAdapter(Activity activity) {
        this.activity = activity;
        GoodService.INSTANCE.observeGoods().subscribe(onGoodsUpdateConsumer());
    }

    @NonNull
    private Consumer<List<Good>> onGoodsUpdateConsumer() {
        return new Consumer<List<Good>>() {
            @Override
            public void accept(List<Good> goods) {
                Collections.sort(goods, getGoodsComparator());
                goodList = goods;
                notifyDataSetChanged();
            }
        };
    }

    @NonNull
    private Comparator<Good> getGoodsComparator() {
        return new Comparator<Good>() {
            @Override
            public int compare(Good good, Good t1) {
                String firstTitle = good.getTitle();
                String secondTitle = t1.getTitle();
                long firstSells = good.getSoldTimes();
                long secondSells = t1.getSoldTimes();

                int compare = Long.compare(secondSells, firstSells);
                return compare == 0 ? firstTitle.compareTo(secondTitle) : compare;
            }
        };
    }

    @Override
    public CheckoutGoodHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        GoodBinding binding = DataBindingUtil.inflate(inflater, R.layout.good, parent, false);
        return new CheckoutGoodHolder(activity, binding);
    }

    @Override
    public void onBindViewHolder(CheckoutGoodHolder holder, int position) {
        holder.bind(goodList.get(position));
    }

    @Override
    public int getItemCount() {
        return goodList.size();
    }
}
