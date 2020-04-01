package com.colivery.engine.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.Firestore
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.FirestoreClient
import javax.validation.constraints.NotBlank
import java.io.FileInputStream
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("!test")
class FirestoreConfig {

    @Bean
    fun createFirebaseApp(@Value("\${google.keyLocation:#{null}}") keyLocation: String?): FirebaseApp {
        val stream = if (keyLocation == null || keyLocation == "") {
            null
        } else if (keyLocation.startsWith("classpath:")) {
            javaClass.getResourceAsStream(keyLocation.substringAfter("classpath:"))
        } else {
            FileInputStream(keyLocation)
        }
        val options = FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.getApplicationDefault())
                .setProjectId("colivery-app")
                .setDatabaseAuthVariableOverride(mapOf("uid" to "matching-api"))
                .setDatabaseUrl("https://colivery-app.firebaseio.com")

        if (stream != null) {
            options.setCredentials(GoogleCredentials.fromStream(stream))
        }

        return FirebaseApp.initializeApp(options.build())
    }

    @Bean
    fun createFirestore(firebaseApp: FirebaseApp): Firestore = FirestoreClient.getFirestore(firebaseApp)
}
