package com.linkurlshorter.urlshortener.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserModifyingResponse {
    private boolean success;
    private String error;
}
