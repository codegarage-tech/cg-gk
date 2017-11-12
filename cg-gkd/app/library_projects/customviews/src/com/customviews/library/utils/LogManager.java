package com.customviews.library.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by rashed on 5/2/16.
 */
public class LogManager {

    public static String readAllLogs() {
        StringBuilder logBuilder = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                logBuilder.append(line + "\n");
            }
        } catch (Exception e) {
        }
        return logBuilder.toString();
    }

    public static String readApplicationLogs() {

        String processId = Integer.toString(android.os.Process
                .myPid());
        StringBuilder builder = new StringBuilder();

        try {
            String[] command = new String[]{"logcat", "-v", "threadtime"};

            Process process = Runtime.getRuntime().exec(command);

            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains(processId)) {
                    builder.append(line + "\n");
                    //Code here
                }
            }
        } catch (Exception ex) {
            Log.e("readApplicationLogs", "getLog failed", ex);
        }

        return builder.toString();
    }

    public static void clearAllLogs() {
        try {
            Process process = new ProcessBuilder()
                    .command("logcat", "-c")
                    .redirectErrorStream(true)
                    .start();
        } catch (Exception e) {
        }
    }
}
