package com.example.demo;

import com.google.cloud.secretmanager.v1.AccessSecretVersionResponse;
import com.google.cloud.secretmanager.v1.SecretManagerServiceClient;
import com.google.cloud.secretmanager.v1.SecretVersionName;

import java.io.IOException;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.springframework.stereotype.Component;

@Component
public class AccessSecret {
    public char[] accessSecretVersion(String projectId, String secretId, String versionId)
            throws IOException {
        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests. After completing all of your requests, call
        // the "close" method on the client to safely clean up any remaining background resources.
        try (SecretManagerServiceClient client = SecretManagerServiceClient.create()) {
            SecretVersionName secretVersionName = SecretVersionName.of(projectId, secretId, versionId);

            // Access the secret version.
            AccessSecretVersionResponse response = client.accessSecretVersion(secretVersionName);

            // Verify checksum. The used library is available in Java 9+.
            // If using Java 8, you may use the following:
            // https://github.com/google/guava/blob/e62d6a0456420d295089a9c319b7593a3eae4a83/guava/src/com/google/common/hash/Hashing.java#L395
            byte[] data = response.getPayload().getData().toByteArray();
            // Calcula el checksum CRC-32C utilizando Guava en Java 8
            HashFunction crc32c = Hashing.crc32c();
            HashCode hashCode = crc32c.hashBytes(data);

            // Compara el checksum calculado con el checksum recibido
            if (response.getPayload().getDataCrc32C() != hashCode.asInt()) {
                System.out.println("Data corruption detected.");
                return null;
            }

            // Print the secret payload.
            //
            // WARNING: Do not print the secret in a production environment - this
            // snippet is showing how to access the secret material.
            // Devuelve el valor del secreto como una cadena
            return response.getPayload().getData().toStringUtf8().toCharArray();
        }
    }
}
