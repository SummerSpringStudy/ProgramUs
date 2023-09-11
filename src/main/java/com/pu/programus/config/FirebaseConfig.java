package com.pu.programus.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.cloud.StorageClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;

@Slf4j
@Service
public class FirebaseConfig {
    @Value("${firebase.configuration.file}")
    private String firebaseConfigPath;

    @PostConstruct
    public void initialize() {
        try {
            FileInputStream serviceAccount =
                    new FileInputStream("src/main/resources/private/serviceAccount.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setStorageBucket("programus-117f2.appspot.com")
                    .build();

            FirebaseApp.initializeApp(options);

        } catch (IOException e) {
            log.error("Firebase init failed" + e.getMessage());
        }
    }
    /*@Bean
    public FirebaseApp firebaseApp() throws IOException {
        FileInputStream inputStream = new FileInputStream("src/main/resources/private/storageKey.json");

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(inputStream))
                .setStorageBucket("programus-11060.appspot.com/")
                .build();

        return FirebaseApp.initializeApp(options);
    }

    @Bean
    public FirebaseAuth firebaseAuth() throws IOException {
        return FirebaseAuth.getInstance(firebaseApp());
    }

    @Bean
    public Bucket bucket() throws IOException {
        return StorageClient.getInstance(firebaseApp()).bucket();
    }*/
}
