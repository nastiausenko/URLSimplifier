package com.linkurlshorter.urlshortener.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * Controller class for handling user-related operations.
 *
 * <p>
 * This class defines REST endpoints for changing user passwords and email addresses.
 * </p>
 * @author Artem Poliakov
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "User", description = "The User API")
@RequestMapping("V1/user")
public class UserController {
    private final UserService userService;
    /**
     * Handles POST requests to change a user's password.
     *
     * @param passRequest the request payload containing the new password.
     * @return a {@link ResponseEntity} indicating the result of the operation.
     * @throws NoSuchEmailFoundException if the user's email is not found.
     */
    @PostMapping("/change-password")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Change user password")
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
    /**
     * Handles POST requests to change a user's email address.
     *
     * @param emailRequest the request payload containing the new email address.
     * @return a {@link ResponseEntity} indicating the result of the operation and including a new JWT token in the response headers.
     * @throws NoSuchEmailFoundException if the user's email is not found.
     */
    @PostMapping("/change-email")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Change user email")
    public ResponseEntity<UserModifyingResponse> changeEmail(@RequestBody ChangeUserEmailRequest emailRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int alteredCount = userService.updateByEmailDynamically(
                User.builder()
                        //TODO: add email validation
                        .email(emailRequest.getNewEmail())
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
