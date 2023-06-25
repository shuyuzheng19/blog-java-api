package com.zsy.admin.utils;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.service.spi.ServiceException;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author 郑书宇
 * @create 2023/6/4 9:33
 * @desc
 */
public class IpUtils {

    private static Searcher searcher;

    static {
        // 解决项目打包找不到ip2region.xdb
        try {
            InputStream inputStream = new ClassPathResource("/ipdb/ip2region.xdb").getInputStream();
            byte[] cBuff = FileCopyUtils.copyToByteArray(inputStream);
            searcher = Searcher.newWithBuffer(cBuff);
        } catch (IOException e) {
            throw new ServiceException("ip2region.xdb加载失败");
        }

    }


    public static String getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

    public static String getIpCity(String ip){
        try {
            String[] result = searcher.search(ip).split("\\|");
            return (result[0]+" "+result[2]).replace("0","").trim();
        } catch (Exception e) {
            return null;
        }
    }
}
