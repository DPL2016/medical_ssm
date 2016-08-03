package com.kaishengit.controller;

import com.kaishengit.dto.FlashMessage;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;


@Controller
public class HomeController {
    /**
     * 去登录页
     * @return
     */
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String index(){
        return "login";
    }

    /**
     * 去主页
     * @return
     */
    @RequestMapping(value = "/home",method = RequestMethod.GET)
    public String home(){
        return "home";
    }

    @RequestMapping(value = "/",method = RequestMethod.POST)
    public String login(String username, String password, RedirectAttributes redirectAttributes, HttpServletRequest request){
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()){
            subject.logout();
        }
        try {
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
            subject.login(usernamePasswordToken);
            return "redirect:/home";
        }catch (LockedAccountException ex){
            redirectAttributes.addFlashAttribute("message",new FlashMessage(FlashMessage.STATE_ERROR,"账号已被禁用"));
        }catch (AuthenticationException e){
            redirectAttributes.addFlashAttribute("message",new FlashMessage(FlashMessage.STATE_ERROR,"账号或密码错误"));
        }
        return "redirect:/";
    }
}
