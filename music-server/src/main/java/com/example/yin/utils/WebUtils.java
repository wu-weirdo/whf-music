package com.example.yin.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * web工具类
 *
 * @author whf
 * @date 2023/04/23
 */
public class WebUtils {
    
    public static String renderString(HttpServletResponse response, String string) {
        try
        {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}