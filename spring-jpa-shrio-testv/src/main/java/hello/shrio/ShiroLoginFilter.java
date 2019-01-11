//package hello.shrio;
//
//import basic.framework.ability.admin.client.utils.AdminFilter;
//import basic.framework.components.core.exception.BaseException;
//import basic.framework.components.core.utils.Jsons;
//import basic.framework.components.core.utils.Springs;
//import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
//import org.springframework.web.util.UrlPathHelper;
//
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//public class ShiroLoginFilter extends FormAuthenticationFilter {
//    private UrlPathHelper urlPathHelper = new UrlPathHelper();
//    /**
//     * 在访问controller前判断是否登录，返回json，不进行重定向。
//     * @param request
//     * @param response
//     * @return true-继续往下执行，false-该filter过滤器已经处理，不继续执行其他过滤器
//     * @throws Exception
//     */
//    @Override
//    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
//
//        AdminFilter adminFilter = Springs.getBean(AdminFilter.class);
//        if (adminFilter != null && adminFilter.isPass(urlPathHelper.getOriginatingRequestUri((HttpServletRequest) request))) {
//            return true;
//        }
//
//
//        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
//
//        httpServletResponse.setCharacterEncoding("UTF-8");
//        httpServletResponse.setContentType("application/json");
//
//        BaseException e = BaseException.create("010006","系统未登录，请重新登录");
//
//        Map<String,String> respMap = new HashMap<>();
//        respMap.put("code",e.getCode());
//        respMap.put("message",e.getMessage());
//        httpServletResponse.getWriter().write(Jsons.DEFAULT.toJson(respMap));
//        /*
//
//        if (isAjax(request)) {
//            httpServletResponse.setCharacterEncoding("UTF-8");
//            httpServletResponse.setContentType("application/json");
//
//
//            BaseException e = BaseException.create("010006","系统未登录，请重新登录");
//
//
//            Map<String,String> respMap = new HashMap<>();
//            respMap.put("code",e.getCode());
//            respMap.put("message",e.getMessage());
//
//            httpServletResponse.getWriter().write(Jsons.DEFAULT.toJson(respMap));
//
//        } else {
//            //saveRequestAndRedirectToLogin(request, response);
//            *//**
//             * @Mark 非ajax请求重定向为登录页面
//             *//*
//            httpServletResponse.sendRedirect("/login");
//        }*/
//        return false;
//    }
//
//    private boolean isAjax(ServletRequest request){
//        String xRequestedWith = ((HttpServletRequest) request).getHeader("X-Requested-With");
//        if("XMLHttpRequest".equalsIgnoreCase(xRequestedWith)){
//            return Boolean.TRUE;
//        }
//
//        String accept = ((HttpServletRequest) request).getHeader("accept");
//        if (accept != null && accept.indexOf("application/json") != -1)
//        {
//            return true;
//        }
//
//        return Boolean.FALSE;
//    }
//
//}