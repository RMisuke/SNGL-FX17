module com.snowchen.snglfx17 {
    requires javafx.controls;
    requires javafx.fxml;
    requires atlantafx.base;
    requires jmccc;
    requires java.desktop;
    requires jmccc.mcdownloader;


    opens com.snowchen.snglfx17 to javafx.fxml;
    exports com.snowchen.snglfx17;
}