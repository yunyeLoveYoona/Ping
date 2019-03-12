package com.yun.ping.grade;

/**
 * 网络延迟
 */
public class NetDelay implements INetGrade {
    private long maxDelay = INetGrade.DELAY;
    private long minDelay = INetGrade.FLOW;


    public void setMaxDelay(long maxDelay) {
        this.maxDelay = maxDelay;
    }

    public void setMinDelay(long minDelay) {
        this.minDelay = minDelay;
    }

    @Override
    public long getMaxDelay() {
        return maxDelay;
    }

    @Override
    public long getMinDelay() {
        return minDelay;
    }

    @Override
    public long getNowDelay() {
        return nowDelay;
    }

    private long nowDelay;

    public void setNowDelay(long nowDelay) {
        this.nowDelay = nowDelay;
    }
}
