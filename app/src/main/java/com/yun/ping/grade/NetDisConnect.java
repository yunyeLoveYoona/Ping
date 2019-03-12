package com.yun.ping.grade;

/**
 * 网络断开连接
 */
public class NetDisConnect implements INetGrade {
    private long maxDelay = Long.MAX_VALUE;
    private long minDelay = INetGrade.DELAY;

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
