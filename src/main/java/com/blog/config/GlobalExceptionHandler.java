package com.blog.config;

import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(NoSuchElementException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public String handleNotFound() {
    return "error/404";
  }

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String handleIllegalArgument(IllegalArgumentException ex, Model model) {
    model.addAttribute("message", ex.getMessage());
    return "error";
  }

}