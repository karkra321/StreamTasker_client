package com.example.trackingapp.Trackers;

import com.example.trackingapp.http.HttpHelper;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseMotionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MouseTracker implements NativeMouseListener, NativeMouseMotionListener {
    private static final String SERVER_URL = "http://localhost:8080/track/mouse";
    private static final Logger logger = LoggerFactory.getLogger(MouseTracker.class);

    public static void startTracking() {
        GlobalScreen.addNativeMouseListener(new MouseTracker());
        GlobalScreen.addNativeMouseMotionListener(new MouseTracker());
        logger.info("MouseTracker started!");
    }

    @Override
    public void nativeMouseMoved(NativeMouseEvent e) {
        String jsonPayload = String.format("{\"eventType\":\"mousemove\",\"x\":%d,\"y\":%d}", e.getX(), e.getY());
        HttpHelper.sendJson(SERVER_URL, jsonPayload);
    }

    @Override
    public void nativeMousePressed(NativeMouseEvent e) {
        String jsonPayload = String.format("{\"eventType\":\"click\",\"x\":%d,\"y\":%d}", e.getX(), e.getY());
        HttpHelper.sendJson(SERVER_URL, jsonPayload);
    }
}
