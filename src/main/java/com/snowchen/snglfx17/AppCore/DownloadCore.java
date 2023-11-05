package com.snowchen.snglfx17.AppCore;

import org.to2mbn.jmccc.mcdownloader.MinecraftDownloader;
import org.to2mbn.jmccc.mcdownloader.MinecraftDownloaderBuilder;
import org.to2mbn.jmccc.mcdownloader.download.concurrent.DownloadCallback;
import org.to2mbn.jmccc.mcdownloader.download.tasks.DownloadTask;
import org.to2mbn.jmccc.mcdownloader.download.concurrent.CallbackAdapter;
import org.to2mbn.jmccc.option.MinecraftDirectory;
import org.to2mbn.jmccc.version.Version;
import org.to2mbn.jmccc.mcdownloader.provider.DefaultLayoutProvider;
import com.snowchen.snglfx17.AppCore.LaunchCore;

public class DownloadCore {
    public class BmclApiProvider extends DefaultLayoutProvider {
    //新增BangBang93API
        @Override
        protected String getLibraryBaseURL() {
            return "http://bmclapi2.bangbang93.com/libraries/";
        }

        @Override
        protected String getVersionBaseURL() {
            return "http://bmclapi2.bangbang93.com/versions/";
        }

        @Override
        protected String getAssetIndexBaseURL() {
            return "http://bmclapi2.bangbang93.com/indexes/";
        }

        @Override
        protected String getVersionListURL() {
            return "http://bmclapi2.bangbang93.com/mc/game/version_manifest.json";
        }

        @Override
        protected String getAssetBaseURL() {
            return "http://bmclapi2.bangbang93.com/assets/";
        }

    }

    public void DownloadGameVersion (){
            // 下载位置（要下载到的.minecraft目录）
            MinecraftDirectory dir = new MinecraftDirectory(LaunchCore.Game_Directory);

            // 创建MinecraftDownloader
            MinecraftDownloader downloader = MinecraftDownloaderBuilder
                    .create()
                    .build();

            // 下载Minecraft1.xx
            downloader.downloadIncrementally(dir, "1.20.1", new CallbackAdapter<Version>() {

                @Override
                public void done(Version result) {
                    // 当完成时调用
                    // 参数代表实际下载到的Minecraft版本
                    System.out.printf("下载完成，下载到的Minecraft版本：%s%n", result);
                }

                @Override
                public void failed(Throwable e) {
                    // 当失败时调用
                    // 参数代表是由于哪个异常而失败的
                    System.out.printf("下载失败%n");
                    e.printStackTrace();
                }

                @Override
                public void cancelled() {
                    // 当被取消时调用
                    System.out.printf("下载取消%n");
                }

                @Override
                public <R> DownloadCallback<R> taskStart(DownloadTask<R> task) {
                    // 当有一个下载任务被派生出来时调用
                    // 在这里返回一个DownloadCallback就可以监听该下载任务的状态
                    System.out.printf("开始下载：%s%n", task.getURI());
                    return new CallbackAdapter<R>() {

                        @Override
                        public void done(R result) {
                            // 当这个DownloadTask完成时调用
                            System.out.printf("子任务完成：%s%n", task.getURI());
                        }

                        @Override
                        public void failed(Throwable e) {
                            // 当这个DownloadTask失败时调用
                            System.out.printf("子任务失败：%s。原因：%s%n", task.getURI(), e);
                        }

                        @Override
                        public void cancelled() {
                            // 当这个DownloadTask被取消时调用
                            System.out.printf("子任务取消：%s%n", task.getURI());
                        }

                        @Override
                        public void retry(Throwable e, int current, int max) {
                            // 当这个DownloadTask因出错而重试时调用
                            // 重试不代表着失败
                            // 也就是说，一个DownloadTask可以重试若干次，
                            // 每次决定要进行一次重试时就会调用这个方法
                            // 当最后一次重试失败，这个任务也将失败了，failed()才会被调用
                            // 所以调用顺序就是这样：
                            // retry()->retry()->...->failed()
                            System.out.printf("子任务重试[%d/%d]：%s。原因：%s%n", current, max, task.getURI(), e);
                        }
                    };
                }
            });
        downloader.shutdown();
        }

    }


