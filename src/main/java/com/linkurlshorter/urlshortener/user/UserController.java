package com.linkurlshorter.urlshortener.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/change-password")
    public ResponseEntity<UserModifyingResponse> changePassword(@RequestBody ChangeUserPasswordRequest passRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int alteredCount = userService.updateByEmailDynamically(
                User.builder()
                        .password(passRequest.getNewPassword())  //TODO: needs to be encoded + password validation
                        .build(),
                authentication.getName()
        );
        if (alteredCount <= 0) {
            throw new NoSuchEmailFoundException();   //TODO: to be handled in global handler
        } else {
            UserModifyingResponse response = new UserModifyingResponse(true, "ok");
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/change-email")
    public ResponseEntity<UserModifyingResponse> changeEmail(@RequestBody ChangeUserEmailRequest emailRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int alteredCount = userService.updateByEmailDynamically(
                User.builder()
                        .email(emailRequest.getNewEmail())    //TODO: add email validation
                        .build(),
                authentication.getName()
        );
        if (alteredCount <= 0) {
            throw new NoSuchEmailFoundException();
        } else {
            UserModifyingResponse response = new UserModifyingResponse(true, "ok");
            return ResponseEntity
                    .ok()
                    .header("Authentication", "...") //TODO: add valid JWT generator
                    .body(response);
        }
    }
}
