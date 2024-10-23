package com.example.blogjava.interceptors;

import com.example.blogjava.Utils.ThreadLocalUtil;
import com.example.blogjava.Utils.TokenUtils;
import com.example.blogjava.common.AuthAccess;
import com.example.blogjava.exception.serviceException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

//----------------------------------登录拦截器-----------------------------------------
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        System.out.println("Intercepted path: " + request.getRequestURI());
        if(handler instanceof HandlerMethod){
            AuthAccess annotation = ((HandlerMethod) handler).getMethodAnnotation(AuthAccess.class);
            if(annotation!= null){
                return true;
            }
        }
        String token = request.getHeader("Authorization");

        try {
            //判断redis中的token是否过期
            ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
            String redistoken = stringStringValueOperations.get(token);
            if(redistoken==null){
                System.out.println("132 "+token);
                throw new RuntimeException();
            }
            Map<String,Object> claims = TokenUtils.parseToken(token);
            System.out.println("登录成功");
            //把业务数据存储到线程池当中
            ThreadLocalUtil.set(claims);
            return true;
        }
        catch (Exception e) {
            //jwt验证失败
            //设置校验码为401
           // System.out.println("123 "+token);
            response.setStatus(401);
            System.out.println("账号未登录");
            throw new serviceException("401","账号未登录或者没有参数");
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ThreadLocalUtil.remove();
    }
}
