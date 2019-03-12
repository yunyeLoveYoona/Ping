package com.yun.ping;


import com.yun.ping.grade.INetGrade;

/**
 * 网速model
 */
public interface INetSpeedModel {
    /**
     * 当前网速级别
     *
     * @return
     */
    public INetGrade getGrade();

    /**
     * 获取当前延迟
     *
     * @return
     */
    public long getDelay();

}
