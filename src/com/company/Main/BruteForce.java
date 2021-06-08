package com.company.Main;

import org.apache.commons.lang3.time.StopWatch;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

 public class BruteForce {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        String[] hashes = {"bf6630505826d6613e15dc0462d243f03d73cc71d0c509c7187d22af9cd50d14",
                "2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824",
                "3a7bd3e2360a3d29eea436fcfb7e44c735d117c42d1c1835420b6b9942dd4f1b"};
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        for (String hash : hashes) {
            executorService.submit(new Forcer(hash));
        }

        executorService.shutdown();
    }
}


class Forcer implements Runnable {

    private static final int LENGTH = 5; //длина пароля..

    private final byte[] crackMe;
    private final String crackMeString;

    private final MessageDigest digest = MessageDigest.getInstance("SHA-256");

    public Forcer(String crackMe) throws NoSuchAlgorithmException {
        this.crackMeString = crackMe;
        this.crackMe = hexStringToByteArray(crackMe);
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
    @Override //до сих пор не понимаю override
    public void run() {
        StopWatch watch = new StopWatch();
        watch.start();
        String match = "";
        char[] chars = new char[LENGTH];
        boolean done = false;

        for (chars[0] = 'a'; chars[0] <= 'z' && !done; chars[0]++) {
            for (chars[1] = 'a'; chars[1] <= 'z' && !done; chars[1]++) {
                for (chars[2] = 'a'; chars[2] <= 'z' && !done; chars[2]++) {
                    for (chars[3] = 'a'; chars[3] <= 'z' && !done; chars[3]++) {
                        for (chars[4] = 'a'; chars[4] <= 'z' && !done; chars[4]++) {
                            String canidate = new String(chars);
                            byte[] hash = digest.digest(canidate.getBytes());
                            if (Arrays.equals(hash, crackMe)) {
                                match = canidate;
                                done = true;
                            }

                        }
                    }
                }
            }
        }
        System.out.printf("Время на поиск: %d\n", watch.getTime(TimeUnit.SECONDS)); //получили время
        System.out.printf("%s является паролем для %s\n", match, crackMeString);
    }
}
