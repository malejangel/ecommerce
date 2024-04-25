package com.icodeap.ecommerce.infrastructure.controller;

import com.icodeap.ecommerce.infrastructure.exception.UserNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        return new ResponseEntity<>("No se puede eliminar este producto porque está siendo referenciado por otras entidades.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //metodo para manejar excepcion de 0 unidades
    //@ExceptionHandler(IndexOutOfBoundsException.class)
    //public ResponseEntity<String> handleIndexOutOfBoundsException(IndexOutOfBoundsException ex) {
        //return new ResponseEntity<>("Se ha producido un error. Por favor, inténtelo de nuevo más tarde.", HttpStatus.INTERNAL_SERVER_ERROR);
   // }
    @ExceptionHandler(IndexOutOfBoundsException.class)
    public String handleIndexOutOfBoundsException() {
        return "user/error";
    }

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFoundException(UserNotFoundException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/login";
    }
}


