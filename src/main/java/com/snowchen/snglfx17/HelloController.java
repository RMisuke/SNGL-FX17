package com.snowchen.snglfx17;

import com.snowchen.snglfx17.AppCore.LaunchCore;
import com.snowchen.snglfx17.AppCore.SystemState;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert;
import cn.hutool.core.io.unit.DataUnit;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import cn.hutool.system.oshi.OshiUtil;
import oshi.hardware.*;

import org.to2mbn.jmccc.launch.LaunchException;
import oshi.SystemInfo;

import java.io.*;
import java.util.Objects;
import java.text.DecimalFormat;
import java.util.Set;

public class HelloController {
    public void initialize() throws InterruptedException {
        // 将标准输出流重定向到TextArea
        System.setOut(new ConsoleOutputStream(System.out, LogArea::appendText));
        // 将标准错误流重定向到TextArea
        System.setErr(new ConsoleOutputStream(System.err, LogArea::appendText));
        System.out.println(" ____  _   _  ____ _     \n" +
                "/ ___|| \\ | |/ ___| |    \n" +
                "\\___ \\|  \\| | |  _| |    \n" +
                " ___) | |\\  | |_| | |___ \n" +
                "|____/|_| \\_|\\____|_____|");
        //SystemState.main();
        InitializeVersionChoice();
        InitializeMaxMem();
        SetMaxMemListen();

    }
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
            System.out.println(SetMaxMem.getValue());
            LaunchCore.Max_Mem = (int) SetMaxMem.getValue();
        if (!Objects.equals(LaunchCore.Player_Name, ""))
        {
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
                } catch (IOException | LaunchException e) {
                    throw new RuntimeException(e);
                }
            }else{
            showAlert("游戏版本不能为空");
            return;}
        }else{
            showAlert("用户名不能为空");}

    }
    @FXML//刷新游戏文件夹按钮
    protected void ReloadGameDirectoryClick(){
        InitializeVersionChoice();
        System.out.println("游戏文件夹已重载");
    }
    private String[] VersionList = {};
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
    private void InitializeMaxMem(){//初始化最大内存设置
        GlobalMemory memory = OshiUtil.getMemory();
        double memoryTotal = memory.getTotal()/ (1024 * 1024);
        int intMemoryTotal = (int) memoryTotal;
        SetMaxMem.setMax(intMemoryTotal);
        SetMaxMem.setValue(intMemoryTotal/4);
        MaxMemInput.setText(String.valueOf(SetMaxMem.getValue()));
        LaunchCore.Max_Mem = (int) SetMaxMem.getValue();
    }
    @FXML
    private TextArea LogArea;//日志输出框

    @FXML
    private void onMaxMemTextChanged() {
        // 监听文本框的值变化，并实时更新滑块的最大值
        try {
            int maxMemValue = Integer.parseInt(MaxMemInput.getText());
            SetMaxMem.setValue(maxMemValue);
            LaunchCore.Max_Mem = maxMemValue;
        } catch (NumberFormatException e) {
            // 处理输入非数字的情况，可以选择给出提示或重置为默认值
            showAlert("请输入有效数字！");
        }
    }

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
    private void SetMaxMemListen() {//监听并更改滑块
        SetMaxMem.valueProperty().addListener((observable, oldValue, newValue) -> {
            MaxMemInput.setText(String.valueOf( newValue));
            LaunchCore.Max_Mem = (int) SetMaxMem.getValue();
        });
    }
    @FXML
    private CheckBox SetFullScreen;

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

    @FXML
    private Slider SetMaxMem;

    @FXML
    private TextField SetJavaEnviroment;

    @FXML
    private ChoiceBox<String> VersionChoice;//版本选择下拉框


}


