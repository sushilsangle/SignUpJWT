package com.sushils.handler;

import java.util.Optional;

import com.sushils.models.User;
import com.sushils.repository.UserRepository;
import com.sushils.security.services.UserServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class LoginAttempt {
	
	private static final Logger logger = LoggerFactory.getLogger(LoginAttempt.class);

	
	@Autowired
	UserServices userService;
	
	@Autowired
	UserRepository userepo;

	 @EventListener
	    public void authSuccessEventListener(AuthenticationSuccessEvent authorizedEvent){
	        // write custom code here for login success audit
         String name= authorizedEvent.getAuthentication().getName();
   
         logger.info("User  login success--->"+name);
	     userService.resetFailedAttempts(name);
	     logger.info("This is success event : "+authorizedEvent.getAuthentication().getPrincipal());
	    }
	 
	   @EventListener
	    public void authFailedEventListener(AbstractAuthenticationFailureEvent oAuth2AuthenticationFailureEvent){
	        // write custom code here login failed audit.
		   logger.info("User  login Failed");
		   logger.info(oAuth2AuthenticationFailureEvent.getAuthentication().getPrincipal().toString());
	        String username=oAuth2AuthenticationFailureEvent.getAuthentication().getPrincipal().toString();
        	Optional<User> user=userepo.findByUsername(username);
            if(user.isPresent()) {
	              if (user.get().isEnabled() && user.get().isAccountNonLocked()) {
				      if (user.get().getFailedAttempt() < UserServices.MAX_FAILED_ATTEMPTS - 1) {
				           userService.increaseFailedAttempts(user.get());
				        } else {
				                   userService.lock(user.get());
				               }
                          }
	                 }
        	   }
         }
