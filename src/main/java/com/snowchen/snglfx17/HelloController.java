package com.snowchen.snglfx17;

import com.snowchen.snglfx17.AppCore.LaunchCore;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert;

import org.to2mbn.jmccc.launch.LaunchException;

import java.io.*;
import java.util.Objects;

public class HelloController {
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("错误");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
        @FXML//启动按钮实现
    protected void StartButtonClick() {
            LaunchCore.Player_Name = PlayerNameInput.getText();
            System.out.println(LaunchCore.Player_Name);
        if (!Objects.equals(LaunchCore.Player_Name, ""))
        {
            LaunchCore.Max_Mem = Integer.parseInt("2048");
            LaunchCore.Game_Version = VersionChoice.getSelectionModel().getSelectedItem();
            System.out.println("选择的游戏版本:"+LaunchCore.Game_Version);
            if (!Objects.equals(LaunchCore.Game_Version, null)){
                LaunchCore.Java_Environment = SetJavaEnviroment.getText();
                System.out.println(SetFullScreen.isSelected());
                LaunchCore.FullScreen_Set = SetFullScreen.isSelected();
                if(!Objects.equals(SetGameDirectory.getText(), LaunchCore.Game_Directory)){
                    LaunchCore.Game_Directory = SetGameDirectory.getText();
                }
                System.out.println("游戏目录"+ LaunchCore.Game_Directory);
                try {
                    new LaunchCore();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (LaunchException e) {
                    throw new RuntimeException(e);
                }
            }
            showAlert("游戏版本不能为空");
            return;
        }
            showAlert("用户名不能为空");

    }
    @FXML
    private TextField PlayerNameInput;//玩家名文本框

    @FXML
    private TextField MaxMemInput;//最大内存文本框

    @FXML
    private TextField ServerAddress;

    @FXML
    private TextField ServerPort;

    @FXML
    private TextField SetGameDirectory;

    @FXML
    private Button ReloadGameDirectory;

    @FXML//刷新游戏文件夹按钮
    protected void ReloadGameDirectoryClick(){
        InitializeVersionChoice();
        System.out.println("游戏文件夹已重载");
    }
    @FXML
    private TextField SetJavaEnviroment;

    @FXML
    private ChoiceBox<String> VersionChoice;//版本选择下拉框
    private String[] VersionList = {};

    public void initialize() {
        InitializeVersionChoice();
        // 将标准输出流重定向到TextArea
        System.setOut(new ConsoleOutputStream(System.out, LogArea::appendText));
        // 将标准错误流重定向到TextArea
        System.setErr(new ConsoleOutputStream(System.err, LogArea::appendText));

    }
    private void InitializeVersionChoice() {
        // 指定目录路径
        String directoryPath = SetGameDirectory.getText()+"/versions/";
        System.out.println(directoryPath);
        // 创建File对象，表示目录
        File directory = new File(directoryPath);

        // 确保指定的路径确实是一个目录
        if (!directory.isDirectory()) {
            System.out.println("指定的路径不是一个目录");
            showAlert("指定的路径不是一个目录");
            return;
        }

        // 使用FilenameFilter过滤出目录（而不是文件）
        FilenameFilter directoryFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                // 接收目录，排除文件
                return new File(dir, name).isDirectory();
            }
        };

        // 获取目录下的所有目录名
        String[] VersionList = directory.list(directoryFilter);

        // 输出获取到的目录名
        for (String directoryName : VersionList) {
            System.out.println(directoryName);
        }
        ObservableList<String> optionList = FXCollections.observableArrayList(VersionList);
        VersionChoice.setItems(optionList);
        VersionChoice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                System.out.println("Selected: " + newValue);
            }
        });
    }

    @FXML
    private TextArea LogArea;//日志输出框

    private static class ConsoleOutputStream extends java.io.PrintStream {
        private java.io.PrintStream original;
        private java.util.function.Consumer<String> consumer;

        public ConsoleOutputStream(java.io.PrintStream original, java.util.function.Consumer<String> consumer) {
            super(original);
            this.original = original;
            this.consumer = consumer;
        }

        @Override
        public void println(final String x) {
            original.println(x); // 保持原始输出功能
            Platform.runLater(() -> consumer.accept(x + "\n")); // 在JavaFX应用程序线程上更新TextArea
        }
    }
    @FXML
    private CheckBox SetFullScreen;


}


