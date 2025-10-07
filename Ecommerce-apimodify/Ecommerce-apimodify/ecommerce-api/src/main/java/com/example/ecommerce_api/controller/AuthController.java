package com.example.ecommerce_api.controller;


import com.example.ecommerce_api.entity.Customer;
import com.example.ecommerce_api.service.CustomerService;
import com.example.ecommerce_api.security.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final RestTemplate restTemplate;
    private final CustomerService customerService;
    private final JwtUtil jwtUtil;

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Value("${google.redirect.uri}")
    private String redirectUri; // set to OAuth Playground redirect

    public AuthController(RestTemplate restTemplate, CustomerService customerService, JwtUtil jwtUtil) {
        this.restTemplate = restTemplate;
        this.customerService = customerService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Exchange authorization code (from OAuth Playground) for Google tokens,
     * fetch userinfo (email + name), create/find Customer, generate JWT and return it.
     *
     * Request (JSON): { "code": "AUTH_CODE_FROM_PLAYGROUND" }
     * Response (JSON): { "customerId": 123, "jwt": "..." }
     */
    @PostMapping("/google")
    public ResponseEntity<?> handleGoogleCode(@RequestBody Map<String, String> body) {
        String code = body.get("code");
        if (code == null || code.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "code is required"));
        }

        try {
            // 1) Exchange code for tokens (POST to https://oauth2.googleapis.com/token)
            String tokenUrl = "https://oauth2.googleapis.com/token";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            String form = "code=" + encode(code)
                    + "&client_id=" + encode(clientId)
                    + "&client_secret=" + encode(clientSecret)
                    + "&redirect_uri=" + encode(redirectUri)
                    + "&grant_type=authorization_code";

            HttpEntity<String> tokenRequest = new HttpEntity<>(form, headers);
            ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(tokenUrl, tokenRequest, Map.class);

            if (!tokenResponse.getStatusCode().is2xxSuccessful() || tokenResponse.getBody() == null
                    || tokenResponse.getBody().get("access_token") == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Failed to exchange code for access_token", "details", tokenResponse.getBody()));
            }

            String accessToken = tokenResponse.getBody().get("access_token").toString();

            // 2) Call Google userinfo endpoint
            String userInfoUrl = "https://www.googleapis.com/oauth2/v2/userinfo"; // or openidconnect endpoint
            HttpHeaders userHeaders = new HttpHeaders();
            userHeaders.setBearerAuth(accessToken);
            HttpEntity<Void> userReq = new HttpEntity<>(userHeaders);

            ResponseEntity<Map> userResp = restTemplate.exchange(userInfoUrl, HttpMethod.GET, userReq, Map.class);
            if (!userResp.getStatusCode().is2xxSuccessful() || userResp.getBody() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Failed to fetch user info", "details", userResp.getBody()));
            }

            Map userBody = userResp.getBody();
            String email = (String) userBody.get("email");
            //String name = (String) userBody.getOrDefault("name", email);

            if (email == null || email.isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "No email in userinfo"));
            }

            // 3) Create or find customer
            Customer customer = customerService.findOrCreateByEmail(email);

            // 4) Generate JWT
            String jwt = jwtUtil.generateToken(email);

            // 5) Return customer id and jwt
            Map<String, Object> resp = new HashMap<>();
            resp.put("customerId", customer.getId());
            resp.put("jwt", jwt);
            resp.put("email", email);
            return ResponseEntity.ok(resp);

        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Server error", "message", ex.getMessage()));
        }
    }

    // helper URLEncoder
    private String encode(String s) {
        try {
            return java.net.URLEncoder.encode(s, java.nio.charset.StandardCharsets.UTF_8);
        } catch (Exception e) { return s; }
    }
}

