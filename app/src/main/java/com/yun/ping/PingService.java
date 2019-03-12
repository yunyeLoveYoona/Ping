package com.yun.ping;

import com.yun.ping.grade.INetGrade;
import com.yun.ping.grade.NetDelay;
import com.yun.ping.grade.NetFlow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * 服务器延迟检测service
 */
public class PingService {
    //自定义网速等级
    private List<INetGrade> iNetGrades;
    //定时轮询ping多主机map
    private Map<List<String>, IPingListener> pingTaskMap;

    private static class PingServiceHolder {
        public static PingService pingService = new PingService();
    }

    public static PingService getService() {
        return PingServiceHolder.pingService;
    }


    private PingService() {
        iNetGrades = new ArrayList<INetGrade>();
        pingTaskMap = new HashMap<>();
    }


    //网络流畅
    private NetFlow netFlow = new NetFlow();

    /**
     * 设置网络流畅等级的延迟
     *
     * @param minDelay
     * @param maxDelay
     */
    public void setNetFlow(long minDelay, long maxDelay) {
        netFlow.setMaxDelay(maxDelay);
        netFlow.setMinDelay(minDelay);
    }


    //网络卡顿
    private NetDelay netDelay = new NetDelay();

    /**
     * 设置网络卡顿等级的延迟
     *
     * @param minDelay
     * @param maxDelay
     */
    public void setNetDelay(long minDelay, long maxDelay) {
        netDelay.setMaxDelay(maxDelay);
        netDelay.setMinDelay(minDelay);
    }

    /**
     * 增加自定义网络等级
     *
     * @param iNetGrade
     */
    public void addNetGrade(INetGrade iNetGrade) {
        iNetGrades.add(iNetGrade);
    }

    /**
     * 清空自定义网络等级
     */
    public void clearNetGrade() {
        iNetGrades.clear();
    }

    /**
     * 增加自动轮询ping 任务
     *
     * @param hostList
     * @param iPingListener
     */
    public void addPingTask(List<String> hostList, IPingListener iPingListener) {
        pingTaskMap.put(hostList, iPingListener);
    }

    /**
     * 取消自动轮询ping 任务
     *
     * @param hostList
     * @param iPingListener
     */
    public void removePingTask(List<String> hostList, IPingListener iPingListener) {
        pingTaskMap.remove(hostList);
    }

    /**
     * 取消所有ping 任务
     */
    public void clearPingTask() {
        pingTaskMap.clear();
    }

    //ping 线程池
    private ExecutorService cachePool = Executors.newCachedThreadPool();

    /**
     * 在线程池中进行ping任务
     * 可通过返回的futureTask中断任务
     * 或者获取任务结果
     *
     * @param ping
     * @return
     */
    public FutureTask<INetGrade> doPing(Ping ping) {
        FutureTask<INetGrade> futureTask = new FutureTask<>(ping);
        cachePool.execute(futureTask);
        return futureTask;
    }


    //正在进行的轮询ping 任务
    private PingTask pingTask;
    //轮询时间
    private long time;
    //ping重试次数
    private int times = 2;
    //ping任务
    private FutureTask<Boolean> futureTask;

    /**
     * 启动 ping轮询任务
     *
     * @param time
     */
    public void startTask(long time) {
        stopTask();
        pingTask = new PingTask(time, pingTaskMap, cachePool, times);
        futureTask = new FutureTask<>(pingTask);
        cachePool.execute(futureTask);
    }


    /**
     * 停止轮询任务
     */
    public void stopTask() {
        if (futureTask != null) {
            futureTask.cancel(true);
        }
        if (pingTask != null) {
            pingTask.stop();
        }
    }

    public NetFlow getNetFlow() {
        return netFlow;
    }

    public NetDelay getNetDelay() {
        return netDelay;
    }
}
