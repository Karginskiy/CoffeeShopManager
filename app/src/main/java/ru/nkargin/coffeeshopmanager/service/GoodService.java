package ru.nkargin.coffeeshopmanager.service;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import ru.nkargin.coffeeshopmanager.model.Good;

public class GoodService {

    public final static GoodService INSTANCE = new GoodService();

    private List<Good> goodsCache = new ArrayList<>();

    private BehaviorSubject<List<Good>> goodsSubject = BehaviorSubject.createDefault(goodsCache);

    public void save(Good good) {
        good.save();
        if (!goodsCache.contains(good)) {
            goodsCache.add(good);
        }
        goodsSubject.onNext(goodsCache);
    }

    public GoodService() {
        goodsCache = Good.listAll(Good.class);
        goodsSubject.onNext(goodsCache);
    }

    public void remove(Good good) {
        good.delete();
        goodsCache.remove(good);
        goodsSubject.onNext(goodsCache);
    }

    public Observable<List<Good>> observeGoods() {
        return goodsSubject;
    }
}
