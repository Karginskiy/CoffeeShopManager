package ru.nkargin.coffeeshopmanager.feature.checkout;

import android.support.annotation.NonNull;

import java.util.Map;
import java.util.Set;

import ru.nkargin.coffeeshopmanager.databinding.ActivityCheckoutBinding;
import ru.nkargin.coffeeshopmanager.model.Good;
import rx.functions.Action1;
import rx.functions.Func1;

public final class CheckoutRxUtils {
    @NonNull
    public static Func1<Map<Good, Integer>, Set<Map.Entry<Good, Integer>>> getMapToEntrySet() {
        return new Func1<Map<Good,Integer>, Set<Map.Entry<Good, Integer>>>() {
            @Override
            public Set<Map.Entry<Good, Integer>> call(Map<Good, Integer> goodIntegerMap) {
                return goodIntegerMap.entrySet();
            }
        };
    }

    @NonNull
    public static Func1<Set<Map.Entry<Good, Integer>>, Integer> getItemsTotalAccumulator() {
        return new Func1<Set<Map.Entry<Good, Integer>>, Integer>() {
            @Override
            public Integer call(Set<Map.Entry<Good, Integer>> entries) {
                int sum = 0;
                for (Map.Entry<Good, Integer> entry : entries) {
                    sum += (entry.getKey().getPrice() * entry.getValue());
                }
                return sum;
            }
        };
    }

    @NonNull
    public static Action1<Integer> getOnTotalsHandler(final ActivityCheckoutBinding checkoutBinding) {
        return new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                checkoutBinding.getViewModel().setSummary(integer);
            }
        };
    }


}
