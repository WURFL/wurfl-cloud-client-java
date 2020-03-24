package com.scientiamobile.wurflcloud;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.*;

public class DetectionPerformanceTest {

    private ICloudClientManager cloudClient;

    @BeforeClass
    public void setup() throws Exception {
        CloudClientLoader loader = new CloudClientLoader(null, "/CloudClientManagerTest.properties");
        cloudClient = loader.getClientManager();
    }

    @Test
    public void performanceTest() throws IOException {
        long elapsed = 0;
        int c = 0;
        BufferedReader br = new BufferedReader(new FileReader("src/test/resources/1000_ua.txt"));
        String line = null;
        long st = System.nanoTime();
        while ((line = br.readLine()) != null && c < 1000){
            cloudClient.getDeviceFromUserAgent(line, null);
            c++;
        }
        elapsed = System.nanoTime() - st;
        System.out.println("-- Detection time: " + elapsed + " nanoseconds -- ");
    }

}
