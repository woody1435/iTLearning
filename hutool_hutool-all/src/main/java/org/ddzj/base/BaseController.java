/**
 * Copyright (C), 2015-2020, 大道至简
 * FileName: BaseController
 * Author:   ddzj
 * Date:     2020/4/4 0:05
 * Description: 控制层基类
 * Version: 0.1.0
 */
package org.ddzj.base;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class BaseController {
    /**
    * @description 获取HttpServletRequest
    * @since 0.1.0
    * @param   ；
    * @return javax.servlet.http.HttpServletRequest
    * @author ddzj
    * @修改人及修改内容
    */
    protected static HttpServletRequest getRequest(){
        return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * @description 获取HttpServletResponse
     * @since 0.1.0
     * @param   ；
     * @return javax.servlet.http.HttpServletResponse
     * @author ddzj
     * @修改人及修改内容
     */
    protected static HttpServletResponse getResponse(){
        return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
    }

    /**
     * @description 获取HttpSession
     * @since 0.1.0
     * @param   ；
     * @return javax.servlet.http.HttpSession
     * @author ddzj
     * @修改人及修改内容
     */
    protected static HttpSession getSession(){
        return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession();
    }
}
