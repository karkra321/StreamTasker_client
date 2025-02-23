package com.example.trackingapp.Trackers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class ScreenCapture {
    private static final Logger logger = LoggerFactory.getLogger(ScreenCapture.class);
    private static final String SERVER_URL = "http://localhost:8080/track/screenshot";
    public static void startTracking() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                captureAndSend();
            }
        }, 0, 5000);
        logger.info("ScreenCapture started!");
    }

    public static void captureAndSend() {
        try {
            Robot robot = new Robot();
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage image = robot.createScreenCapture(screenRect);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpeg", baos);
            byte[] imageBytes = baos.toByteArray();

            sendToServer(imageBytes);
        } catch (Exception e) {
            logger.error("Error capturing and sending screenshot", e);
        }
    }

    public static void sendToServer(byte[] imageBytes) throws IOException {
        String boundary = "----" + UUID.randomUUID().toString();
        URL url = new URL(SERVER_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        try (OutputStream outputStream = connection.getOutputStream();
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"), true)) {

            writer.append("--").append(boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"screenshot.jpeg\"\r\n");
            writer.append("Content-Type: image/jpeg\r\n\r\n");
            writer.flush();

            outputStream.write(imageBytes);
            outputStream.flush();
            writer.append("\r\n").flush();

            writer.append("--").append(boundary).append("--\r\n").flush();
        }
    }
}