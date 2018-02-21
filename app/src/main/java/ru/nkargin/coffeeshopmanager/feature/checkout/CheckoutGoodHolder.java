package ru.nkargin.coffeeshopmanager.feature.checkout;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;

import ru.nkargin.coffeeshopmanager.databinding.GoodBinding;
import ru.nkargin.coffeeshopmanager.feature.admin.GoodViewHolder;
import ru.nkargin.coffeeshopmanager.model.Good;
import ru.nkargin.coffeeshopmanager.service.OrderService;

class CheckoutGoodHolder extends GoodViewHolder {
    public CheckoutGoodHolder(Activity activity, GoodBinding binding) {
        super(activity, binding);
    }

    @NonNull
    @Override
    protected View.OnClickListener addOnItemClickListener(final Good good) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderService.INSTANCE.getCurrentOrder().addItem(good);
            }
        };
    }
}
