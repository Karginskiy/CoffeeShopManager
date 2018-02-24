package ru.nkargin.coffeeshopmanager.feature.admin;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import io.reactivex.functions.Consumer;
import ru.nkargin.coffeeshopmanager.R;
import ru.nkargin.coffeeshopmanager.databinding.GoodBinding;
import ru.nkargin.coffeeshopmanager.model.Good;
import ru.nkargin.coffeeshopmanager.service.GoodService;


class GoodListAdapter extends RecyclerView.Adapter<GoodViewHolder> {

    private final Activity activity;
    private List<Good> goodList;

    public GoodListAdapter(Activity activity) {
        this.activity = activity;
        GoodService.INSTANCE.observeGoods().subscribe(onGoodsUpdateConsumer());
    }

    @NonNull
    private Consumer<List<Good>> onGoodsUpdateConsumer() {
        return new Consumer<List<Good>>() {
            @Override
            public void accept(List<Good> goods) {
                goodList = goods;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public GoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        GoodBinding binding = DataBindingUtil.inflate(inflater, R.layout.good, parent, false);
        return new GoodViewHolder(activity, binding);
    }

    @Override
    public void onBindViewHolder(GoodViewHolder holder, int position) {
        holder.bind(goodList.get(position));
    }

    @Override
    public int getItemCount() {
        return goodList.size();
    }
}
