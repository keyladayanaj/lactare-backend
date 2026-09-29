package com.github.lara.sodre.ms_lactech.controller;
import com.github.lara.sodre.ms_lactech.dto.*;
import com.github.lara.sodre.ms_lactech.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/v1/auth")
public class AuthController {
 private final AuthService service; public AuthController(AuthService s){service=s;}
 @PostMapping("/login") public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO dto){return ResponseEntity.ok(service.login(dto));}
 @PostMapping("/logout") public ResponseEntity<Void> logout(@RequestHeader(value="Authorization",required=false) String h){if(h!=null&&h.startsWith("Bearer "))service.logout(h.substring(7));return ResponseEntity.noContent().build();}
}
