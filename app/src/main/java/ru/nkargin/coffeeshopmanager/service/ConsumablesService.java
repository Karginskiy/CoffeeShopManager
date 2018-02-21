package ru.nkargin.coffeeshopmanager.service;

import rx.Observable;
import rx.subjects.BehaviorSubject;

public class ConsumablesService {

    public static final ConsumablesService INSTANCE = new ConsumablesService();

    private BehaviorSubject<Integer> cupsSubject = BehaviorSubject.create();

    public void setCups(int count) {
        cupsSubject.onNext(count);
    }

    public Observable<Integer> observeCupsCount() {
        return cupsSubject.asObservable();
    }
}
