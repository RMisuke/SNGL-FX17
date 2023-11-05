package com.snowchen.snglfx17;

import atlantafx.base.theme.NordDark;
import atlantafx.base.theme.NordLight;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalTime;


public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        LocalTime now = LocalTime.now();
        LocalTime sevenPm = LocalTime.of(19, 0); // 晚上7点
        LocalTime sixAm = LocalTime.of(6, 0); // 早上6点

        if (now.isAfter(sevenPm) || now.isBefore(sixAm)) {
            Application.setUserAgentStylesheet(new NordDark().getUserAgentStylesheet());
            System.out.println("晚上好");
        }else {
        Application.setUserAgentStylesheet(new NordLight().getUserAgentStylesheet());}
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 675, 450);
        stage.setTitle("SNGL Alpha Test");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}