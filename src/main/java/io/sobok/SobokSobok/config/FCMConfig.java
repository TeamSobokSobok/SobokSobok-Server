package io.sobok.SobokSobok.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Configuration
public class FCMConfig {

    @Value("${fcm.key.path}")
    private String SERVICE_ACCOUNT_PATH;

    @Bean
    FirebaseMessaging firebaseMessaging() {
        FirebaseApp firebaseApp = getOrCreateFirebaseApp();
        return FirebaseMessaging.getInstance(firebaseApp);
    }

    private FirebaseApp getOrCreateFirebaseApp() {
        List<FirebaseApp> firebaseApps = FirebaseApp.getApps();
        return firebaseApps.stream()
                .filter(fApp -> FirebaseApp.DEFAULT_APP_NAME.equals(fApp.getName()))
                .findFirst()
                .orElseGet(this::initializeFirebaseApp);
    }


    private FirebaseApp initializeFirebaseApp() {
        try {
            InputStream serviceAccount = new ClassPathResource(SERVICE_ACCOUNT_PATH).getInputStream();
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            return FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            log.error("Firebase App 초기화에 실패했습니다.");
            throw new IllegalArgumentException("Invalid firebase service account");
        }
    }
}
