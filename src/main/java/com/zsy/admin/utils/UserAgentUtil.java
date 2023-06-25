package com.zsy.admin.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserAgentUtil {

    // 正则表达式用于匹配浏览器和设备信息
    private static final Pattern BROWSER_PATTERN = Pattern.compile("(?i)(msie|firefox|chrome|safari|opera)[\\s\\/]?([\\d\\w\\.-]*)");
    private static final Pattern DEVICE_PATTERN = Pattern.compile("(?i)(mobile|mobi|tablet|tv|android|iphone|ipad|ipod|windows|phone)");

    /**
     * 解析浏览器信息
     *
     * @param userAgent User-Agent字符串
     * @return 浏览器名称和版本，格式为 "浏览器名称/版本"
     */
    public static String parseBrowser(String userAgent) {
        Matcher matcher = BROWSER_PATTERN.matcher(userAgent);
        if (matcher.find()) {
            String browserName = matcher.group(1);
            String browserVersion = matcher.group(2);
            return browserName + "/" + browserVersion;
        }
        return "Unknown";
    }

    /**
     * 解析设备信息
     *
     * @param userAgent User-Agent字符串
     * @return 设备名称
     */
    public static String parseDevice(String userAgent) {
        Matcher matcher = DEVICE_PATTERN.matcher(userAgent);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "Unknown";
    }

}