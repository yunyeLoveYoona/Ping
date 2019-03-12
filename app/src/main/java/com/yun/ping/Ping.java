package com.yun.ping;

import com.yun.ping.grade.INetGrade;
import com.yun.ping.grade.NetDisConnect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.Callable;

/**
 * ping任务
 */
public class Ping implements Callable<INetGrade> {
    //主机
    private String host;


    public Ping(String host) {
        this.host = host;
    }

    /**
     * 设置主机
     *
     * @param host
     * @return
     */
    public static Ping onHost(String host) {
        Ping ping = new Ping(host);
        return ping;
    }

    //超时时间
    private long timeOutSecond;

    /**
     * 设置超时时间 单位秒
     *
     * @param timeOutSecond
     * @return
     */
    public Ping setTimeOut(long timeOutSecond) {
        this.timeOutSecond = timeOutSecond;
        return this;
    }

    //重试次数
    private int times;

    /**
     * 设置尝试次数
     *
     * @param times
     * @return
     */
    public Ping setTimes(int times) {
        this.times = times;
        return this;
    }

    //结果监听
    private IPingListener iPingListener;

    /**
     * 设置监听
     *
     * @param iPingListener
     * @return
     */
    public Ping listener(IPingListener iPingListener) {
        this.iPingListener = iPingListener;
        return this;
    }

    /**
     * 在当前线程进行ping操作
     * 不要在主线程中使用
     * 否则会导致主线程阻塞或者其他异常
     *
     * @return
     */
    public INetGrade doPing() {
        return this.call();
    }


    @Override
    public INetGrade call() {
        INetGrade iNetGrade = null;
        Process p = null;
        String ip;
        try {
            if (IpUtil.isUrl(host)) {
                URL url = new URL(host);
                String[] hosts = IpUtil.parseHostGetIPAddress(url.getHost());
                if (hosts != null && hosts.length > 0) {
                    ip = hosts[0];
                } else {
                    ip = null;
                }
            } else {
                ip = host;
            }
            if (ip != null) {
                String command = new StringBuilder("/system/bin/ping -c ").append(times).append(" -w ").append(timeOutSecond).append(" ").append(ip)
                        .toString();
                p = Runtime.getRuntime().exec(command);
                BufferedReader buf = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String str = new String();
                while ((str = buf.readLine()) != null) {
                    if (str.contains("avg")) {
                        int i = str.indexOf("/", 20);
                        int j = str.indexOf(".", i);
                        long delay = Long.parseLong(str.substring(i + 1, j));
                        if (delay < PingService.getService().getNetFlow().getMaxDelay()) {
                            iNetGrade = PingService.getService().getNetFlow();
                            iNetGrade.setNowDelay(delay);
                        } else if (delay < PingService.getService().getNetDelay().getMaxDelay()) {
                            iNetGrade = PingService.getService().getNetDelay();
                            iNetGrade.setNowDelay(delay);
                        } else {
                            iNetGrade = new NetDisConnect();
                            iNetGrade.setNowDelay(delay);
                        }
                    }
                }
                if (iNetGrade == null) {
                    iNetGrade = new NetDisConnect();
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        if (iPingListener != null) {
            iPingListener.onPingResult(host, iNetGrade);
        }
        return iNetGrade;
    }
}
