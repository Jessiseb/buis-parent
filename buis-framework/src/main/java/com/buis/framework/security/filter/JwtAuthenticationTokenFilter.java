package com.buis.framework.security.filter;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import cn.dev33.satoken.exception.SaTokenException;
import cn.hutool.json.JSONUtil;
import com.buis.common.constant.HttpStatus;
import com.buis.common.core.domain.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.buis.common.core.domain.model.LoginUser;
import com.buis.common.utils.SecurityUtils;
import com.buis.common.utils.StringUtils;
import com.buis.framework.web.service.TokenService;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * token过滤器 验证token有效性
 *
 * @author ruoyi
 */
@Component
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter
{
    @Autowired
    private TokenService tokenService;
    private HandlerExceptionResolver resolver;

    @Autowired
    public void setResolver(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException
    {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser) && StringUtils.isNull(SecurityUtils.getAuthentication()))
        {
            tokenService.verifyToken(loginUser);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        try {
            chain.doFilter(request, response);
        } catch (SaTokenException e) {
            log.error("SaTokenException",e);
            writeJson(response, HttpStatus.SUCCESS, AjaxResult.error(HttpStatus.UNAUTHORIZED,"未登录或登录失效，请登录"));
        }
    }
    public static void writeJson(HttpServletResponse response, int httpCode, Object data) {
        response.setStatus(httpCode);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try (PrintWriter out = response.getWriter()) {
            out.print(JSONUtil.toJsonStr(data));
            out.flush();
        } catch (IOException e) {
            log.info("输出异常", e);
        }
    }
}
