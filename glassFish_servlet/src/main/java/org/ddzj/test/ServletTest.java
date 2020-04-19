/**
 * Copyright (C), 2015-2020, yourchoice
 * FileName: ServletTest
 * Author:   ddzj
 * Date:     2020/4/2 14:00
 * Description: 测试servlet
 * Version: 0.1.0
 */
package org.ddzj.test;

import org.ddzj.base.BaseServlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ServletTest")
public class ServletTest extends BaseServlet {

    /**
    * @description 测试servlet
    * @since 0.1.0
    * @param request 参数0；response 参数1；
    * @return void
    * @author ddzj
    * @修改人及修改内容
    */
    public void perform(HttpServletRequest request, HttpServletResponse response){
        request.getRequestURL();
        response.setCharacterEncoding("UTF-8");
        System.out.println("测试");
    }
}
