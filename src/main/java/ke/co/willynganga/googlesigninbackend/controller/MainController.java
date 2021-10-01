package ke.co.willynganga.googlesigninbackend.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@RestController
@RequestMapping("/")
public class MainController {

    private Logger logger = LoggerFactory.getLogger(MainController.class);

    private static final HttpTransport transport = new NetHttpTransport();
    private static final JsonFactory jsonFactory = new GsonFactory();

    @Value("${google.clientID}")
    private static final String CLIENT_ID = "";

    @PostMapping("/auth/google/user")
    public String saveUser(@RequestParam String idToken) throws GeneralSecurityException, IOException {

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singleton(CLIENT_ID))
                .build();

        GoogleIdToken id = verifier.verify(idToken);

        if (id != null) {
            GoogleIdToken.Payload payload = id.getPayload();

            String userId = payload.getSubject();
            String email = payload.getEmail();
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");
            String locale = (String) payload.get("locale");
            String familyName = (String) payload.get("family_name");
            String givenName = (String) payload.get("given_name");

            System.out.println("UserId: " + userId);
            System.out.println("Email: " + email);
            System.out.println("Name: " + name);
            System.out.println("PictureUrl: " + pictureUrl);
            System.out.println("Locale: " + locale);
        } else  {
            System.out.println("Invalid ID token.");
        }

        return "Saved successfully!";
    }


}
