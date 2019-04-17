package com.tim.shopm.utils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StringFormatUtil {
    private static DecimalFormat moneyFormat = new DecimalFormat(",###,##0.00");
    private static DecimalFormat moneyFormatEdit = new DecimalFormat("##0.00");

    public static Date timeToDate(String timestr, String pattern) {
        try {
            return new SimpleDateFormat(pattern, Locale.getDefault()).parse(timestr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String dateToTime(Date timestr, String pattern) {
        return new SimpleDateFormat(pattern, Locale.getDefault()).format(timestr);
    }

    public static String formatMoney(float money) {
        return "￥"+moneyFormat.format(money);
    }
    public static String formatMoneyEdit(float money) {
        return moneyFormatEdit.format(money);
    }
}
