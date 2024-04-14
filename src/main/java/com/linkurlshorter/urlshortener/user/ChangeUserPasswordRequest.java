package com.linkurlshorter.urlshortener.user;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class ChangeUserPasswordRequest {
    private String newPassword;
}
