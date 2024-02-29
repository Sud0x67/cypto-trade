/* (C)2024  Sud0x67@github.com */
package com.sud0x67.cryptotrade.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HelloController {
  @GetMapping("/hello")
  public void sayHello(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    response.getOutputStream().print("hello, quanter!");
  }
}
