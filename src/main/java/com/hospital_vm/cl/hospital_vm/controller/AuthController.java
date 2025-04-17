package com.hospital_vm.cl.hospital_vm.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital_vm.cl.hospital_vm.model.User;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @PostMapping
  public ResponseEntity<?> guardar(@RequestBody User user) {
    Map<String, String> response = new HashMap<>();

    if ("test123@gmail.com".equals(user.getEmail()) && "Pas123".equals(user.getPassword())) {
      response.put("message", "Login exitoso");
      return ResponseEntity.ok(response);
    }

    response.put("message", "Email o contraseña incorrectos");
    return ResponseEntity.badRequest().body(response);
  }

}
