package com.icodeap.ecommerce.application.service;

import jakarta.servlet.http.HttpSession;

public class LogoutService {
    public LogoutService() {
    }
    // Método para cerrar la sesión
    public void logout(HttpSession httpSession){
        httpSession.removeAttribute("iduser");
    }
}
