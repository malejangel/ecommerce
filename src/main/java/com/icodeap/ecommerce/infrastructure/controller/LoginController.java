package com.icodeap.ecommerce.infrastructure.controller;

import com.icodeap.ecommerce.application.service.LoginService;
import com.icodeap.ecommerce.domain.User;
import com.icodeap.ecommerce.infrastructure.dto.UserDto;
import com.icodeap.ecommerce.infrastructure.exception.UserNotFoundException;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController {
    private final LoginService loginService;
    private final UserDetailsService userDetailsService;


    public LoginController(LoginService loginService, UserDetailsService userDetailsService) {
        this.loginService = loginService;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping
    public String login(){
        return "login";
    }

    @GetMapping("/access")
    public String access(RedirectAttributes attributes, HttpSession httpSession, RedirectAttributes flash, Model model){
        User user = loginService.getUser( Integer.parseInt( httpSession.getAttribute("iduser").toString() ) ) ;
        attributes.addFlashAttribute("id", httpSession.getAttribute("iduser").toString() );
        if(loginService.existUser(user.getEmail())){
            if (loginService.getUserType(user.getEmail()).name().equals("ADMIN")){
                return "redirect:/admin";
            }else{
                return "redirect:/home";
            }
        }
        return "redirect:/home";
    }

    @PostMapping("/login")
    public String login(String username, String password, RedirectAttributes redirectAttributes) {
        try {
            userDetailsService.loadUserByUsername(username);
            return "redirect:/home";
        } catch (UserNotFoundException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/login";
        }
    }


}
