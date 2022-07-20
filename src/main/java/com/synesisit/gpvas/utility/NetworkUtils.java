package com.synesisit.gpvas.utility;

import javax.servlet.http.HttpServletRequest;
import java.util.StringTokenizer;

public class NetworkUtils {

    public static String getClientIpAddress(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        String xRealIpHeader = request.getHeader("X-Real-IP");
        String xForwardedForHostHeader = request.getHeader("X-Forwarded-Host");

        System.out.println("X-Forwarded-For: " + xForwardedForHeader);
        System.out.println("X-Real-IP: " + xRealIpHeader);
        System.out.println("X-Forwarded-For-Host: " + xForwardedForHostHeader);
        if (xForwardedForHeader == null) {
            return request.getRemoteAddr();
        } else {
            // As of https://en.wikipedia.org/wiki/X-Forwarded-For
            // The general format of the field is: X-Forwarded-For: client, proxy1, proxy2 ...
            // we only want the client
            return xForwardedForHeader.trim().split(",")[1];
        }
    }
}
