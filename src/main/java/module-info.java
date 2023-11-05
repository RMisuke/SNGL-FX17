module com.snowchen.snglfx17 {
    requires javafx.controls;
    requires javafx.fxml;
    requires atlantafx.base;
    requires jmccc;
    requires java.desktop;
    requires jmccc.mcdownloader;
    requires com.github.oshi;
    requires hutool.all;


    opens com.snowchen.snglfx17 to javafx.fxml;
    exports com.snowchen.snglfx17;
}