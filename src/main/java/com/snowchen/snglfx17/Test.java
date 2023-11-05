package com.snowchen.snglfx17;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Test {
    public static void main(String[] args) {
        ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", "/usr/libexec/java_home -V");
        try {
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}