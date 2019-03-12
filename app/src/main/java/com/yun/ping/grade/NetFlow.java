package com.yun.ping.grade;

/**
 * 网络流畅
 */
public class NetFlow implements INetGrade {
    private long maxDelay = INetGrade.FLOW;
    private long minDelay = 0;

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
