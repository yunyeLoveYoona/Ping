package com.yun.ping;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ip工具类
 */
public class IpUtil {

    /**
     * 域名转ip
     *
     * @param host
     * @return
     */
    public static String[] parseHostGetIPAddress(String host) {
        String[] ipAddressArr = null;
        try {
            InetAddress[] inetAddressArr = InetAddress.getAllByName(host);
            if (inetAddressArr != null && inetAddressArr.length > 0) {
                ipAddressArr = new String[inetAddressArr.length];
                for (int i = 0; i < inetAddressArr.length; i++) {
                    ipAddressArr[i] = inetAddressArr[i].getHostAddress();
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
        return ipAddressArr;
    }

    /**
     * 判断是否为合法IP * @return the ip
     */
    public static boolean isIp(String ipAddress) {
        String ip = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pattern = Pattern.compile(ip);
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();
    }

    /**
     * 是否是url
     *
     * @param url
     * @return
     */
    public static boolean isUrl(String url) {
        Pattern pattern = Pattern
                .compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+$");
        return pattern.matcher(url).matches();
    }

}
