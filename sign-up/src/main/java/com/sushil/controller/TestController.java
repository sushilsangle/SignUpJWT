package com.sushil.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*",maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/all")
    public String allAccess()
    {
        return "Public Content";
    }
    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String userAccess()
    {
        return "User Content";
    }
    @GetMapping("/mod")
    @PreAuthorize("hasRole('MODERATOR')")
    public String modertorAccess()
    {
        return "Moderatr Board";
    }
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess()
    {
        return "Admin Board";
    }
}
