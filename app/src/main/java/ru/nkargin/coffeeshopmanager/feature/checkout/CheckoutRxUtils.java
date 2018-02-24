package ru.nkargin.coffeeshopmanager.feature.checkout;

import android.support.annotation.NonNull;

import java.util.Map;
import java.util.Set;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import ru.nkargin.coffeeshopmanager.databinding.ActivityCheckoutBinding;
import ru.nkargin.coffeeshopmanager.model.Good;

public final class CheckoutRxUtils {
    @NonNull
    public static Function<Map<Good, Integer>, Set<Map.Entry<Good, Integer>>> getMapToEntrySet() {
        return new Function<Map<Good,Integer>, Set<Map.Entry<Good,Integer>>>() {
            @Override
            public Set<Map.Entry<Good, Integer>> apply(Map<Good, Integer> goodIntegerMap) {
                return goodIntegerMap.entrySet();
            }
        };
    }

    @NonNull
    public static Function<Set<Map.Entry<Good, Integer>>, Integer> getItemsTotalAccumulator() {
        return new Function<Set<Map.Entry<Good, Integer>>, Integer>() {
            @Override
            public Integer apply(Set<Map.Entry<Good, Integer>> entries) {
                int sum = 0;
                for (Map.Entry<Good, Integer> entry : entries) {
                    sum += (entry.getKey().getPrice() * entry.getValue());
                }
                return sum;
            }
        };
    }

    @NonNull
    public static Consumer<Integer> getOnTotalsHandler(final ActivityCheckoutBinding checkoutBinding) {
        return new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                checkoutBinding.getViewModel().setSummary(integer);
            }
        };
    }


}
