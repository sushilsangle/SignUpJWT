package com.sushil.controller;

import com.sushil.Payload.request.LoginRequest;
import com.sushil.Payload.request.SignupRequest;
import com.sushil.Payload.response.JwtResponse;
import com.sushil.Payload.response.MessageResponse;
import com.sushil.model.ERole;
import com.sushil.model.Role;
import com.sushil.repository.RoleRepository;
import com.sushil.repository.UserRepository;
import com.sushil.security.jwt.JwtUtils;
import com.sushil.services.UserDetailsImpl;
import com.sushil.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*",maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository  roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item->item.getAuthority())
                .collect(Collectors.toList());
        return ResponseEntity.ok(new JwtResponse(jwt,userDetails.getId(),
                                                    userDetails.getUsername(),
                                                    userDetails.getEmail(),roles));
    }
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest)
    {
        if (userRepository.existsByUsername(signupRequest.getUsername ())){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken ! "));
        }
        if (userRepository.existsByEmail(signupRequest.getEmail())){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use! "));
        }
        User user = new User(signupRequest.getUsername(),
                            signupRequest.getEmail(), passwordEncoder.encode(signupRequest.getPassword()));


        Set<Role> strRoles = signupRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles ==null)
        {
            Role userRoles = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(()->new RuntimeException("Error: Role is not found."));
            roles.add(userRoles);

        }else {
            strRoles.forEach(role->{
                if ("admin".equals(role)) {
                    Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                    roles.add(adminRole);
                } else if ("mod".equals(role)) {
                    Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                    roles.add(modRole);
                } else {
                    Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                    roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

}
