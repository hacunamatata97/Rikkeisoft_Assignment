package com.example.hacunamatata.rikkeisoft_assignment.utils;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;

@SuppressLint("SimpleDateFormat")
public class Utils {
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    public static SimpleDateFormat dayOfWeekFormat = new SimpleDateFormat("EEEE");
    public static SimpleDateFormat fullDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public static SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");
    public static SimpleDateFormat normalDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    public static SimpleDateFormat pictureNameDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
    public static SimpleDateFormat shortDateFormat = new SimpleDateFormat("dd/MM HH:mm");
}
