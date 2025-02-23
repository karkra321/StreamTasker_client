module com.example.trackingapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires com.github.kwhat.jnativehook;
    requires com.sun.jna.platform;
    requires com.sun.jna;
    requires org.slf4j;


    opens com.example.trackingapp to javafx.fxml;
    exports com.example.trackingapp;
    exports com.example.trackingapp.Trackers;
    opens com.example.trackingapp.Trackers to javafx.fxml;
    exports com.example.trackingapp.http;
    opens com.example.trackingapp.http to javafx.fxml;
}