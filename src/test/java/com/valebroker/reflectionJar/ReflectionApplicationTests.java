package com.valebroker.reflectionJar;

import com.valebroker.reflectionJar.service.ClassMaintainer;
import com.valebroker.reflectionJar.service.EncriptaDecriptaAES;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReflectionApplicationTests {

	@Autowired
	ClassMaintainer classMaintainer;

	@Autowired
	EncriptaDecriptaAES encriptaDecriptaAES;

	String originalFile = System.getProperty("user.dir") + "/src/EncriptaDecriptaAES.ser";

	String classeDeserializada = System.getProperty("user.dir") + "/src/EncriptaDecriptaAES.des";
	String encryptedFile = System.getProperty("user.dir") + "/src/java.bin";
	String tempDecryptedFile = System.getProperty("user.dir") + "/src/temp.out";


	Object classe;

	Key chave = null;

	@BeforeAll
	void generateKey() throws NoSuchAlgorithmException {
		chave = EncriptaDecriptaAES.generateKey();
	}
	@Test
	void justCallEncryptMethod(){

		String teste = EncriptaDecriptaAES.encrypt("RAFAEL COLVARA",chave);
		System.out.println(teste);
	}
	@Test
	void SerializaClasseEmArquivo() {
		classMaintainer.serializeClass(EncriptaDecriptaAES.class,originalFile);
	}

	@Test
	void DeserializaEmArquivo() {
		classMaintainer.serializeClass(originalFile, classeDeserializada);
	}


	@Test
	void EncryptJavaFile() throws Exception {
		classMaintainer.encryptClass(originalFile, encryptedFile, classMaintainer.checkDigest(originalFile));
	}

	@Test
	void DecryptJavaFile() throws Exception {
		classMaintainer.decryptClass(encryptedFile, tempDecryptedFile, classMaintainer.checkDigest(originalFile));
	}

	@Test
	void GetClassFromDeserializedFile() {
		try {
			classe = classMaintainer.getClassFromDeserializedFile(tempDecryptedFile);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	@Test
	void callMethodAndRemoveFile(){
		try {
			classe = classMaintainer.getClassFromDeserializedFile(tempDecryptedFile);
			String resultado = classMaintainer.callMethodFromObject(classe,"encrypt", "Rafael Colvara");
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}


}
