package com.example.trackingapp;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class GlobalHookManager {
    private static final Logger LOGGER = Logger.getLogger(GlobalHookManager.class.getName());

    public static void initialize() {
        try {
            Logger nativeHookLogger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
            nativeHookLogger.setLevel(Level.OFF);

            synchronized (GlobalScreen.class) {
                if (!GlobalScreen.isNativeHookRegistered()) {
                    LOGGER.info("Registering GlobalScreen...");
                    GlobalScreen.registerNativeHook();
                }
            }

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    LOGGER.info("Unregistering GlobalScreen...");
                    GlobalScreen.unregisterNativeHook();
                } catch (NativeHookException e) {
                    LOGGER.log(Level.SEVERE, "Error while unregistering GlobalScreen", e);
                }
            }));

        } catch (NativeHookException e) {
            LOGGER.log(Level.SEVERE, "Error while registering the native hook", e);
        }
    }
}

