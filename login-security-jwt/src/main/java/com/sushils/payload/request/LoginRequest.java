package com.sushils.payload.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


public class LoginRequest {

	@Email(message = "Email should be valid")
    @NotBlank(message = "UserName is mandatory")
	private String username;

    @Pattern(regexp="(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}",
            message="Must contain at least one  number and one uppercase and lowercase letter, and at least 8 or more characters")
    @NotBlank(message = "Password is mandatory")
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
