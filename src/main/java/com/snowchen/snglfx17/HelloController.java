package com.snowchen.snglfx17;

import com.snowchen.snglfx17.LaunchCore.AppCore;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import org.to2mbn.jmccc.launch.LaunchException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.io.*;
import java.util.Objects;

public class HelloController {

    @FXML//启动按钮实现
    protected void StartButtonClick() {
        AppCore.Player_Name = PlayerNameInput.getText();
        AppCore.Max_Mem = Integer.parseInt(MaxMemInput.getText());
        AppCore.Game_Version = VersionChoice.getSelectionModel().getSelectedItem();
        System.out.println(SetFullScreen.isSelected());
        AppCore.FullScreen_Set = SetFullScreen.isSelected();
        if(!Objects.equals(SetGameDirectory.getText(), AppCore.Game_Directory)){
            AppCore.Game_Directory = SetGameDirectory.getText();
        }
        System.out.println("游戏目录"+AppCore.Game_Directory);
        try {
                new AppCore();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (LaunchException e) {
                throw new RuntimeException(e);
            }

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
    private Label LauncherInfo;

    @FXML
    private ChoiceBox<String> VersionChoice;//版本选择下拉框
    private String[] VersionList = {};

    public void initialize() {
        InitializeVersionChoice();
        LauncherInfo.setText("SnowのLauncher ver 0.1 Alpha");


    }
    private void InitializeVersionChoice() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                String directoryPath = SetGameDirectory.getText()+"/versions/";
                System.out.println(directoryPath);

                // 创建File对象，表示目录
                File directory = new File(directoryPath);

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
                // 确保指定的路径确实是一个目录
                if (!directory.isDirectory()) {
                    System.out.println("指定的路径不是一个目录");
                    //return;
                }
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
                    System.out.println(AppCore.Game_Version);
                });

            }
        }, 0, 5000); // 首次运行延迟0毫秒，然后每5000毫秒运行一次
    }

    @FXML
    private TextArea LogArea;//日志输出框

    @FXML
    public void LogCatch() {
        try {
            // 执行日志捕获逻辑，这里只是示例，捕获控制台输出
            ProcessBuilder builder = new ProcessBuilder("java", "-version");
            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                LogArea.appendText(line + "\n");
            }
        } catch (Exception e) {
            LogArea.appendText("日志捕获出现异常：\n" + e.getMessage());
        }

    }
    @FXML
    private CheckBox SetFullScreen;


}


