package com.yun.ping;

import com.yun.ping.grade.INetGrade;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;

/**
 * ping 定时任务
 */
public class PingTask implements Callable<Boolean> {
    //轮询时间
    private long time;
    //定时轮询ping多主机map
    private Map<List<String>, IPingListener> pingTask;
    //线程池中的ping任务
    private List<FutureTask<INetGrade>> taskList;
    //线程池
    private ExecutorService executorService;
    //每次ping的重试次数
    private int times;
    private int timeOutSecond = 30;


    /**
     * @param time            轮询时间
     * @param pingTask
     * @param executorService
     * @param times           重试次数
     */
    protected PingTask(long time, Map<List<String>, IPingListener> pingTask
            , ExecutorService executorService, int times) {
        this.time = time;
        this.pingTask = pingTask;
        taskList = new ArrayList<FutureTask<INetGrade>>();
        this.executorService = executorService;
        this.times = times;
    }

    @Override
    public Boolean call() {
        while (!isStop) {
            for (List<String> hostList : pingTask.keySet()) {
                IPingListener iPingListener = pingTask.get(hostList);
                for (String host : hostList) {
                    FutureTask<INetGrade> futureTask = new FutureTask<>(
                            Ping.onHost(host).listener(iPingListener).setTimes(times).setTimeOut(timeOutSecond));
                    taskList.add(futureTask);
                    executorService.execute(futureTask);
                }
            }
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
                stop();
            }
        }
        for (FutureTask futureTask : taskList) {
            futureTask.cancel(true);
        }
        pingTask = null;
        taskList = null;
        return true;
    }


    private boolean isStop = false;

    /**
     * 设置停止
     */
    public void stop() {
        isStop = true;
    }
}
