package com.snowchen.snglfx17.AppCore;

import org.to2mbn.jmccc.auth.OfflineAuthenticator;
import org.to2mbn.jmccc.launch.LaunchException;
import org.to2mbn.jmccc.launch.Launcher;
import org.to2mbn.jmccc.launch.LauncherBuilder;
import org.to2mbn.jmccc.launch.ProcessListener;
import org.to2mbn.jmccc.option.JavaEnvironment;
import org.to2mbn.jmccc.option.LaunchOption;
import org.to2mbn.jmccc.option.MinecraftDirectory;

import org.to2mbn.jmccc.option.WindowSize;
import org.to2mbn.jmccc.util.ExtraArgumentsTemplates;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LaunchCore {
    public static String Game_Version,Server_Address,Java_Environment = "";
    public static String Player_Name = "";
    public static String Game_Directory = ".minecraft";
    public static int Max_Mem,Server_Port;
    public static boolean FullScreen_Set = false;
    private static LaunchOption option;

    public LaunchCore() throws IOException ,LaunchException{
        Launcher launcher = LauncherBuilder.create()
                .printDebugCommandline(true) //是否将启动参数输出至控制台
                .nativeFastCheck(true) //是否开启快速检查Natives文件
                .build(); //构建Laucher对象
        try {
             option = new LaunchOption(Game_Version,new OfflineAuthenticator(Player_Name),
                    new MinecraftDirectory(Game_Directory));//启动指令
            GameOption();//启动设置
            launcher.launch(option, new ProcessListener()
            {
                public void onLog(String log) {
                    System.out.println(log);
                } //正常日志
                public void onErrorLog(String log) {
                    System.out.println(log);
                } //错误日志
                public void onExit(int code) {
                    System.out.println("状态码：" + code);
                } //状态码
            });
        }catch (IOException e) {
            e.printStackTrace();//捕获异常抛出
        }
        catch (LaunchException e) {
            e.printStackTrace();//
        }
    }
    private void GameOption()//游戏设置定义
    {
        File javaEnvir = new File("/Library/Java/JavaVirtualMachines/zulu-17.jdk/Contents/Home/bin/java");//设置Java目录
        option.setJavaEnvironment(new JavaEnvironment(javaEnvir));

        option.setMaxMemory(Max_Mem); //最大内存
        option.setMinMemory(0); //最小内存
        if (FullScreen_Set){
            option.setWindowSize(WindowSize.fullscreen()); //全屏
        }else {
            option.setWindowSize(WindowSize.window(854, 480)); //窗口化，宽854高480
        }
        option.extraJvmArguments().add(ExtraArgumentsTemplates.FML_IGNORE_INVALID_MINECRAFT_CERTIFICATES);
        option.extraJvmArguments().add(ExtraArgumentsTemplates.FML_IGNORE_PATCH_DISCREPANCISE);//忽略FML标签
        //option.setServerInfo(new ServerInfo("liyuuserver.com",1200));
        Map<String, String> vars = new HashMap<String, String>();
        vars.put("version_type", "Snowの启动器");
        option.commandlineVariables().putAll(vars);


    }

}