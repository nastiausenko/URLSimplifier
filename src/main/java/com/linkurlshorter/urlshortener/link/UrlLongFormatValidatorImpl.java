package com.linkurlshorter.urlshortener.link;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;


/**
 * Implementation {@link ConstraintValidator} of a validator to check the URL format.
 * Validates the URL format using a regular expression.
 *
 * @author Vlas Pototskyi
 */

public class UrlLongFormatValidatorImpl implements ConstraintValidator<UrlLongFormatValidator, String> {
    /**
     * Checks if the entered string matches the URL format.
     *
     * @param url     String to be validated.
     * @param context Validation context that can be used to collect errors.
     * @return true if the string is a valid URL, false otherwise.
     */
    @Override
    public boolean isValid(String url, ConstraintValidatorContext context) {
        if (isUrlNullOrEmpty(url)) {
            context.buildConstraintViolationWithTemplate("Url must not be null or empty!")
                    .addConstraintViolation();
            context.disableDefaultConstraintViolation();
            return false;
        }
        if (validateUrl(url)) {
            context.buildConstraintViolationWithTemplate("Invalid URL format!")
                    .addConstraintViolation();
            context.disableDefaultConstraintViolation();
            return false;
        }
        if (!isUrlActive(url)) {
            context.buildConstraintViolationWithTemplate("Url not active!")
                    .addConstraintViolation();
            context.disableDefaultConstraintViolation();
            return false;
        }
        return true;
    }

    /**
     * Validates the URL format using a regular expression.
     *
     * @param url The URL string to be validated.
     * @return true if the URL string matches the standard URL format, false otherwise.
     */
    private static boolean validateUrl(String url) {
        String urlRegex = "^https?://\\S+$";
        return !url.matches(urlRegex);
    }

    /**
     * Checks if the provided URL is null or empty.
     *
     * @param url The URL string to be checked.
     * @return true if the URL string is null or empty, false otherwise.
     */
    private static boolean isUrlNullOrEmpty(String url) {
        return url == null || url.isEmpty();
    }

    /**
     * Checks if the provided URL is active by attempting to establish a connection.
     *
     * @param urlStr The URL string to check for activity.
     * @return {@code true} if the URL is active (responds with a 200 status code), {@code false} otherwise.
     */
    private boolean isUrlActive(String urlStr) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(urlStr)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        } catch (IOException e) {
            return false;
        }
    }
}

