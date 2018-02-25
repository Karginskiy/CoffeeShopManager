package ru.nkargin.coffeeshopmanager.service;

import android.util.Pair;

import java.util.Calendar;

import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.SECOND;

/**
 * Created by hei on 25.02.2018.
 */

public class ServiceUtils {

    public static void setDayToMaximum(Calendar date) {
        date.set(HOUR_OF_DAY, date.getActualMaximum(HOUR_OF_DAY));
        date.set(MINUTE, date.getActualMaximum(MINUTE));
        date.set(SECOND, date.getActualMaximum(SECOND));
        date.set(Calendar.MILLISECOND, date.getActualMaximum(SECOND));
    }

    public static void setDayToMinimum(Calendar date) {
        date.set(HOUR_OF_DAY, date.getActualMinimum(HOUR_OF_DAY));
        date.set(MINUTE, date.getActualMinimum(MINUTE));
        date.set(SECOND, date.getActualMinimum(SECOND));
        date.set(Calendar.MILLISECOND, date.getActualMinimum(SECOND));
    }

}
