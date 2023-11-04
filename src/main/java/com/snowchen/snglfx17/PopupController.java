package com.snowchen.snglfx17;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;

public class PopupController {
    @FXML
    private Button CloseButton;

    @FXML
    private Text TheText;

    @FXML
    private void initialize() {
        // 设置Text控件的文字
        TheText.setText("这是要显示的文字");
    }
    @FXML
    public void HandleClose(javafx.event.ActionEvent actionEvent) {
        // 获取当前窗口的Stage实例
        Stage stage = (Stage) CloseButton.getScene().getWindow();

        // 关闭窗口
        stage.close();
    }
}
