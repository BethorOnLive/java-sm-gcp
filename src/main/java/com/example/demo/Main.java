package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.*;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Component
public class Main implements CommandLineRunner {
	@Autowired
	AccessSecret secrets;
	@Autowired
	AccessSecretFile files;
	final static String projectId = "sample-project-407519";
	final static String secretId = "secret-pfx-password";
	final static String versionId = "2";
	final static String secretFileId = "pfx-file";
	final static String secretIdFile = "1";
	// PFX's password

	static X509Certificate certificate = null;

	@Override
	public void run(String... args) throws Exception {
		// Agrega aquí el código que deseas ejecutar al iniciar la aplicación
		System.out.println("La aplicación se ha iniciado y esta clase se ejecuta automáticamente.");

		char[] secreto = secrets.accessSecretVersion(projectId, secretId, versionId);
		File secretFile = files.accessSecretVersion(projectId, secretFileId, secretIdFile);
		System.out.println(secretFile);
		System.out.println(secreto);

		// Get certificate from PFX file
		certificate = getCertificate(secretFile);

		System.out.println("Obtener certificado: " + certificate);
	}

	/**
	 * Get a certificate through a pfx file
	 *
	 * @param file
	 * @return
	 * @throws KeyStoreException
	 * @throws IOException
	 * @throws CertificateException
	 * @throws NoSuchAlgorithmException
	 */
	public X509Certificate getCertificate(File file) throws
			KeyStoreException,
			IOException,
			CertificateException,
			NoSuchAlgorithmException
	{
		char[] secreto = secrets.accessSecretVersion(projectId, secretId, versionId);
		File secretFile = files.accessSecretVersion(projectId, secretFileId, secretIdFile);

		KeyStore ks = KeyStore.getInstance("PKCS12");
		ks.load(new FileInputStream(secretFile), secreto);
		String alias = ks.aliases().nextElement();

		return (X509Certificate) ks.getCertificate(alias);
	}
}
