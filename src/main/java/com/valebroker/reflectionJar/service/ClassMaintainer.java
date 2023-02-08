package com.valebroker.reflectionJar.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@Service
public class ClassMaintainer implements Serializable {
    public void encryptClass(String inputFile, String outputFile, byte[] expectedDigest) throws  Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        FileInputStream in = new FileInputStream(inputFile);
        FileOutputStream out = new FileOutputStream(outputFile);
        byte[] buffer = new byte[8192];
        int length;
        while ((length = in.read(buffer)) != -1) {
            md.update(buffer, 0, length);
            out.write(buffer, 0, length);
        }
        byte[] digest = md.digest();
        in.close();
        out.close();
        if (!MessageDigest.isEqual(digest, expectedDigest)) {
            throw new Exception("Encrypt do arquivo Java falhou!");
        }
    }
    public void decryptClass(String inputFile, String outputFile, byte[] expectedDigest) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        FileInputStream in = new FileInputStream(inputFile);
        FileOutputStream out = new FileOutputStream(outputFile);
        byte[] buffer = new byte[8192];
        int length;
        while ((length = in.read(buffer)) != -1) {
            md.update(buffer, 0, length);
            out.write(buffer, 0, length);
        }
        byte[] digest = md.digest();
        in.close();
        out.close();
        if (!MessageDigest.isEqual(digest, expectedDigest)) {
            throw new Exception("Decrypt do arquivo java falhou");
        }
    }

    public Object getClassFromDeserializedFile(String decryptedFile) throws IOException, ClassNotFoundException {

        FileInputStream fis = new FileInputStream(decryptedFile);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object obj = ois.readObject();
        ois.close();
        fis.close();
        return obj;

    }

    public String callMethodFromObject(Object deserializedClass, String methodName, String argument) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Class<?> classToCallMethodOn = deserializedClass.getClass();
        Method methodToCall = classToCallMethodOn.getMethod(methodName, String.class);
        Object result = methodToCall.invoke(null, argument);

        return result.toString();
    }

    private boolean removeTmpFile(String arquivo){
        File file = new File(arquivo);
        return  (file.delete());
    }

    public byte[] checkDigest(String arquivoOriginal) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        FileInputStream in = new FileInputStream(arquivoOriginal);

        byte[] buffer = new byte[8192];
        int length;
        while ((length = in.read(buffer)) != -1) {
            md.update(buffer, 0, length);
        }
        byte[] originalDigest = md.digest();
        in.close();
        return originalDigest;
    }

    public <T> void serializeClass(T classe, String nomeArquivo) {

        try {
            FileOutputStream fos = new FileOutputStream(nomeArquivo);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(classe);
            oos.close();
            fos.close();
            System.out.println("Serialization successful");
        } catch (Exception e) {
            System.out.println("Serialization failed");
            e.printStackTrace();
        }
    }

}
