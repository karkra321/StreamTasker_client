package com.example.trackingapp.Trackers;

import com.example.trackingapp.http.HttpHelper;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;

public class ActiveWindowTracker {
    private static final Logger logger = LoggerFactory.getLogger(ActiveWindowTracker.class);
    private static final String SERVER_URL = "http://localhost:8080/track/window";
    public static void startTracking() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                trackActiveWindow();
            }
        }, 0, 5000);
        logger.info("ActiveWindowTracker started!");
    }

    public static void trackActiveWindow() {
        String activeWindow = getActiveWindowTitle();
        if (activeWindow != null && !activeWindow.isEmpty()) {
            HttpHelper.sendJson(SERVER_URL, String.format("{\"window\":\"%s\"}", activeWindow));
        }
    }

    public static String getActiveWindowTitle() {
        char[] buffer = new char[1024];

        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            WinDef.HWND hwnd = User32.INSTANCE.GetForegroundWindow();
            User32.INSTANCE.GetWindowText(hwnd, buffer, 1024);
            return Native.toString(buffer);
        } else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            try {
                Process process = Runtime.getRuntime().exec(
                        new String[]{"/usr/bin/osascript", "-e", "tell application \"System Events\" to get name of (processes where frontmost is true)"});
                process.waitFor();
                byte[] output = process.getInputStream().readAllBytes();
                return new String(output).trim();
            } catch (Exception e) {
                logger.error("Error getting active window title on Mac", e);
            }
        }

        return "Unknown window";
    }
}