package com.yun.ping.grade;

/**
 * 网速等级接口
 */
public interface INetGrade {
    //流畅500 毫秒
    public static final long FLOW = 500;
    //延迟 3000毫秒
    public static final long DELAY = 3000;

    /**
     * 对应的最大延迟
     *
     * @return
     */
    public long getMaxDelay();

    /**
     * 对应的最小延迟
     *
     * @return
     */
    public long getMinDelay();


    /**
     * 当前延迟
     *
     * @return
     */
    public long getNowDelay();


    /**
     * 设置当前延迟
     */
    public void setNowDelay(long delay);

}
