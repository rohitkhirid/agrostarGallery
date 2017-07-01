/***
 * This is free and unencumbered software released into the public domain.
 * <p>
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 * <p>
 * For more information, please refer to <http://unlicense.org/>
 */

package rohitkhirid.com.galleryappagrostar.utils;

import android.util.Log;

import java.text.SimpleDateFormat;

import rohitkhirid.com.galleryappagrostar.BuildConfig;

/**
 * NOTE : OPEN SOURCE FILE : COPIED FROM INTERNET
 */
public class DebugLog {

    static String className;
    static String methodName;

    public final static int LOGS_MAX_LENGTH = 1000;

    public static CircularBuffer<String> sCircularBuffer = new CircularBuffer(LOGS_MAX_LENGTH);

    public static boolean isDebuggable() {
        return BuildConfig.DEBUG;
    }

    private static String createLog(String log) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        buffer.append(methodName);
        buffer.append("] ");
        buffer.append(log);

        return buffer.toString();
    }

    private static void getMethodNames(StackTraceElement[] sElements) {
        className = sElements[1].getFileName();
        methodName = sElements[1].getMethodName();
    }

    public static void e(String message) {
        // Throwable instance must be created before any methods
        getMethodNames(new Throwable().getStackTrace());
        String logMessage = createLog(message);
        sCircularBuffer.add(getDateTime() + "  E/" + className + ": " + logMessage);
        if (!isDebuggable()) {
            return;
        }
        Log.e(className, logMessage);
    }

    public static void i(String message) {
        getMethodNames(new Throwable().getStackTrace());
        String logMessage = createLog(message);
        sCircularBuffer.add(getDateTime() + "  I/" + className + ": " + logMessage);
        /*if (!isDebuggable()) {
            return;
        }*/
        Log.i(className, logMessage);
    }

    public static void d(String message) {
        getMethodNames(new Throwable().getStackTrace());
        String logMessage = createLog(message);
        sCircularBuffer.add(getDateTime() + "  D/" + className + ": " + logMessage);
        if (isDebuggable()) {
            Log.d(className, logMessage);
        }
    }

    public static void v(String message) {
        getMethodNames(new Throwable().getStackTrace());
        String logMessage = createLog(message);
        sCircularBuffer.add(getDateTime() + "  V/" + className + ": " + logMessage);
        if (isDebuggable()) {
            Log.v(className, logMessage);
        }
    }

    public static void w(String message) {
        getMethodNames(new Throwable().getStackTrace());
        String logMessage = createLog(message);
        sCircularBuffer.add(getDateTime() + "  W/" + className + ": " + logMessage);
        if (!isDebuggable()) {
            return;
        }
        Log.w(className, logMessage);
    }

    public static void wtf(String message) {
        getMethodNames(new Throwable().getStackTrace());
        String logMessage = createLog(message);
        sCircularBuffer.add(getDateTime() + "  WTF/" + className + ": " + logMessage);
        if (!isDebuggable()) {
            return;
        }
        Log.wtf(className, logMessage);
    }

    public static String getDateTime() {
        long currentTime = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
        return simpleDateFormat.format(currentTime);
    }
}
