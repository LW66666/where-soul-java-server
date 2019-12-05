package com.where.soul.common.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.sun.deploy.net.HttpResponse;
import com.sun.xml.internal.bind.v2.runtime.output.Encoded;
import com.where.soul.common.Constant;
import com.where.soul.common.LoginManager;
import com.where.soul.common.Result;
import com.where.soul.common.ResultEnum;
import com.where.soul.common.exception.BizException;
import com.where.soul.common.util.GeneratorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * @author lw
 */
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    private LoginManager loginManager;

    public AuthInterceptor(LoginManager loginManager) {
        this.loginManager = loginManager;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String token = request.getHeader(Constant.W_TOKEN);
        if (StringUtils.isEmpty(token)) {
            Cookie[] cookies = request.getCookies();
            for (Cookie c : cookies) {
                if (Constant.W_TOKEN.equals(c.getName())) {
                    token = c.getValue();
                }
            }
        }
        if (StringUtils.isEmpty(token)) {
            return false;
        }
        // 设置用户 id 至 request
        request.setAttribute(Constant.ID, loginManager.get(token));
        boolean isLogin = loginManager.isLogin(token);

        if (!isLogin) {
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            try (PrintWriter writer = response.getWriter()) {
                writer.print(JSONObject.toJSONString(Result.error(ResultEnum.NO_AUTH)));
                writer.flush();
            } catch (IOException e) {
                log.error("response error", e);
            }
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }
}