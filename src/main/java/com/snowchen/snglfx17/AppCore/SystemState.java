package com.snowchen.snglfx17.AppCore;
import cn.hutool.core.io.unit.DataUnit;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import cn.hutool.system.oshi.OshiUtil;
import oshi.hardware.*;

import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SystemState {

    public static void main() throws InterruptedException {
            GlobalMemory memory = OshiUtil.getMemory();

            HardwareAbstractionLayer hardware = OshiUtil.getHardware();
            Console.log("系统信息:");
            Console.log("CPU: {}", OshiUtil.getProcessor().getProcessorIdentifier().getName());
            Console.log("内存: {} ", formatData(memory.getTotal()));
            List<GraphicsCard> graphicsCards = hardware.getGraphicsCards();
            graphicsCards.forEach(e->{
                Console.log("显卡: {}", e.getName());
            });
            Console.log("cpu使用率: {}",  getCpuRate());

            //内容总量
            long total = memory.getTotal();
            //剩余内存
            long available = memory.getAvailable();
            //使用量
            long used = total - available;
            Console.log("内存: {}/{},占用率率: {}",formatData(used),formatData(total),formatRate(used * 1.0 / total));

            Console.log("网络： {}",net());

            //List<UsbDevice> usbDevices = hardware.getUsbDevices(false);
            //usbDevices.forEach(e->{
            //    Console.log("usb: {}",e.getName());
            //});
        }

        /**
         *
         * @return cpuRate cpu使用率
         * @throws InterruptedException
         */
        public static String getCpuRate() throws InterruptedException {
            CentralProcessor processor = OshiUtil.getHardware().getProcessor();
            long[] prevTicks = processor.getSystemCpuLoadTicks();
            // 睡眠1s
            TimeUnit.SECONDS.sleep(1);
            long[] ticks = processor.getSystemCpuLoadTicks();
            long nice = ticks[CentralProcessor.TickType.NICE.getIndex()]
                    - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
            long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()]
                    - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
            long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()]
                    - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
            long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()]
                    - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
            long cSys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()]
                    - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
            long user = ticks[CentralProcessor.TickType.USER.getIndex()]
                    - prevTicks[CentralProcessor.TickType.USER.getIndex()];
            long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()]
                    - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
            long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()]
                    - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
            long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;
            return new DecimalFormat("#.##%").format((cSys + user) * 1.0 / totalCpu);
        }

        public static String net() throws InterruptedException {
            HardwareAbstractionLayer hardware = OshiUtil.getHardware();
            List<NetworkIF> networkIFs = hardware.getNetworkIFs();

            long up = 0;
            long down =0;
            long time=0;

            for (int i = 0; i < networkIFs.size(); i++) {
                NetworkIF net = networkIFs.get(i);
                long bytesSent = net.getBytesSent();
                long bytesRecv = net.getBytesRecv();
                long timeStamp = net.getTimeStamp();
                up += bytesSent;
                down += bytesRecv;
                time += timeStamp;
            }
            TimeUnit.SECONDS.sleep(1);

            networkIFs = hardware.getNetworkIFs();
            long upload = 0;
            long download =0;
            long time_=0;
            for (int i = 0; i < networkIFs.size(); i++) {
                NetworkIF net = networkIFs.get(i);
                long bytesSent = net.getBytesSent();
                long bytesRecv = net.getBytesRecv();
                long timeStamp = net.getTimeStamp();
                upload += bytesSent;
                download += bytesRecv;
                time_ += timeStamp;
            }

            String downloadStr = formatData((download - down) / (time_ - time) * 1000)+"/s";
            String uploadStr =formatData((upload - up) / (time_ - time) * 1000) + "/s";
            return StrUtil.format("↓: {} ↑: {}",downloadStr,uploadStr);
        }

        /**
         * 格式化输出百分比
         * @param rate
         * @return
         */
        public static String formatRate(double rate){
            return new DecimalFormat("#.##%").format(rate);
        }

        /**
         * 格式化输出大小 B/KB/MB...
         * @param size
         * @return
         */
        public static String formatData(long size){
            if (size <= 0L) {
                return "0B";
            } else {
                int digitGroups = Math.min(DataUnit.UNIT_NAMES.length - 1, (int) (Math.log10((double) size) / Math.log10(1024.0D)));
                return (new DecimalFormat("#,##0.##")).format((double) size / Math.pow(1024.0D, (double) digitGroups)) + " " + DataUnit.UNIT_NAMES[digitGroups];
            }
        }
    }

