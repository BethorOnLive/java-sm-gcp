package com.example.demo;

import com.google.cloud.secretmanager.v1.AccessSecretVersionResponse;
import com.google.cloud.secretmanager.v1.SecretManagerServiceClient;
import com.google.cloud.secretmanager.v1.SecretVersionName;

import java.io.File;
import java.io.IOException;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.springframework.stereotype.Component;
import java.io.FileOutputStream;
import java.io.IOException;

@Component
public class AccessSecretFile {

    String filePath = "archivo.pfx";
    public File accessSecretVersion(String projectId, String secretId, String versionId)
            throws IOException {
        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests. After completing all of your requests, call
        // the "close" method on the client to safely clean up any remaining background resources.
        try (SecretManagerServiceClient client = SecretManagerServiceClient.create()) {
            SecretVersionName secretVersionName = SecretVersionName.of(projectId, secretId, versionId);

            // Access the secret version.
            AccessSecretVersionResponse response = client.accessSecretVersion(secretVersionName);
            byte[] data = response.getPayload().getData().toByteArray();
            File pfxFile = new File(filePath);

            try (FileOutputStream fos = new FileOutputStream(pfxFile)) {
                // Writing the bytes to the file
                fos.write(data);
                System.out.println("File has been written successfully");
                return pfxFile;
            } catch (IOException e) {
                // Handle exception
                System.err.println("An error occurred: " + e.getMessage());
                return null;
            }
        }
    }
}
