package com.yun.ping;


import com.yun.ping.grade.INetGrade;

/**
 * ping监听
 */
public interface IPingListener {
    /**
     * ping 返回
     *
     * @param host
     * @param iNetGrade
     */
    public void onPingResult(String host, INetGrade iNetGrade);
}
