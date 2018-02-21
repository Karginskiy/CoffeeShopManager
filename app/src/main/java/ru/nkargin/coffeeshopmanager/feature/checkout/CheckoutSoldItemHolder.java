package ru.nkargin.coffeeshopmanager.feature.checkout;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.Map;

import ru.nkargin.coffeeshopmanager.databinding.SoldListItemBinding;
import ru.nkargin.coffeeshopmanager.model.Good;
import ru.nkargin.coffeeshopmanager.service.OrderService;

class CheckoutSoldItemHolder extends RecyclerView.ViewHolder{
    private final SoldListItemBinding binding;

    public CheckoutSoldItemHolder(final SoldListItemBinding binding) {
        super(binding.getRoot());

        this.binding = binding;
        this.binding.setViewModel(new CheckoutSoldListItemViewModel());

        addPlusButtonClickListener(binding);
        addMinusButtonClickListener(binding);
    }

    private void addPlusButtonClickListener(final SoldListItemBinding binding) {
        binding.plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderService.INSTANCE.getCurrentOrder().addItem(binding.getViewModel().getGood());
            }
        });
    }

    private void addMinusButtonClickListener(final SoldListItemBinding binding) {
        binding.minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderService.INSTANCE.getCurrentOrder().removeItem(binding.getViewModel().getGood());
            }
        });
    }

    public void bind(Map.Entry<Good, Integer> entry) {
        binding.getViewModel().setGood(entry.getKey());
        binding.getViewModel().setCount(entry.getValue());
        binding.executePendingBindings();
    }
}
