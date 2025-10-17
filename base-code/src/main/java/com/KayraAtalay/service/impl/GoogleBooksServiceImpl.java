package com.KayraAtalay.service.impl;

import com.KayraAtalay.dto.googlebooks.GoogleBookItem;
import com.KayraAtalay.dto.googlebooks.GoogleBooksApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;


@Service
public class GoogleBooksServiceImpl {

    // --- 1. REQUIRED COMPONENTS ---
    // These are the essential tools this service needs to function.

    private final RestTemplate restTemplate; // The tool for making HTTP requests to external APIs.
    private final String apiKey; // The API key for authenticating our requests with Google.
    private static final String GOOGLE_BOOKS_API_URL = "https://www.googleapis.com/books/v1/volumes"; // The constant base URL.


    // --- 2. CONSTRUCTOR INJECTION ---
    // We securely obtain the required components from Spring's context.

    public GoogleBooksServiceImpl(RestTemplate restTemplate, @Value("${google.books.api.key}") String apiKey) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey; // The @Value annotation injects the key from application.properties.
    }

    /**
     * Searches for books on the Google Books API based on a given query term.
     * @param query The text entered by the admin (e.g., "Stephen King" or "The Lord of the Rings").
     * @return A list of found book items.
     */
    @SuppressWarnings("deprecation")
	public List<GoogleBookItem> searchBooks(String query) {

        // --- 4. SECURE URL CONSTRUCTION ---
        // We use UriComponentsBuilder to safely construct the URL, which handles special characters automatically.
        String url = UriComponentsBuilder.fromHttpUrl(GOOGLE_BOOKS_API_URL)
                .queryParam("q", query)                 // Query parameter: ?q=Stephen King
                .queryParam("key", apiKey)              // API key parameter: &key=AIza...
                .queryParam("maxResults", 20)           // Fetch only the first 20 results.
                .toUriString();                         // Convert the final result to a URL string.


        // --- 5. API REQUEST AND RESPONSE HANDLING ---
        // We send a GET request and automatically map the JSON response to our GoogleBooksApiResponse class.
        GoogleBooksApiResponse response = restTemplate.getForObject(url, GoogleBooksApiResponse.class);


        // --- 6. SAFETY CHECK AND RETURNING THE RESULT ---
        // This check prevents NullPointerExceptions if the response from Google is empty or malformed.
        if (response != null && response.getItems() != null) {
            return response.getItems(); // Return the list of books.
        } else {
            return Collections.emptyList(); // If nothing is found, return an empty list.
        }
    }

    /**
     * Fetches the full details of a single book using its unique Google Books ID.
     * @param googleBookId The Google ID of the book selected by the admin (e.g., "XYZ123ABC").
     * @return A single book item object.
     */
    @SuppressWarnings("deprecation")
	public GoogleBookItem findBookById(String googleBookId) {

        // Construct the URL specifically for fetching a single volume by its ID.
        String url = UriComponentsBuilder.fromHttpUrl(GOOGLE_BOOKS_API_URL)
                .path("/{id}")                 // Appends a path segment like /XYZ123
                .queryParam("key", apiKey)
                .buildAndExpand(googleBookId)  // Replaces the {id} placeholder with the actual googleBookId
                .toUriString();

        // Map the response directly to GoogleBookItem, as we expect a single book object.
        return restTemplate.getForObject(url, GoogleBookItem.class);
    }
}