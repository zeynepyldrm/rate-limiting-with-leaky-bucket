package com.zeynepyldrm.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LeakyBucketServiceTest {

    @InjectMocks
    LeakyBucketService leakyBucketService;


    @Test
    void givenBucketNotFull_whenAddRequestBucket_thenAddedRequestBucket() {
        boolean result = leakyBucketService.addRequestBucket();
        Assertions.assertTrue(result);
        Assertions.assertEquals(1, leakyBucketService.getCurrentWater());
    }

    @Test
    void givenBucketFull_whenAddRequestBucket_thenAddedRequestBucket() {
        for (int i = 0; i < 20; i++) {
            leakyBucketService.addRequestBucket();
        }
        Assertions.assertEquals(20, leakyBucketService.getCurrentWater());
        boolean result = leakyBucketService.addRequestBucket();
        Assertions.assertFalse(result);
        Assertions.assertEquals(20, leakyBucketService.getCurrentWater());
    }

    @Test
    public void testThreadSafetyWithSynchronizedMethod() throws InterruptedException {
        Runnable requestTask = () -> leakyBucketService.addRequestBucket();
        Thread thread1 = new Thread(requestTask);
        Thread thread2 = new Thread(requestTask);

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        Assertions.assertTrue(leakyBucketService.getCurrentWater() <= leakyBucketService.getCapacity());
    }

    @Test
    void givenBucketNotFull_whenLeakRequestFromBucket_thenShouldDecreaseByLeakRate(){
        for (int i = 0; i < 10; i++) {
            leakyBucketService.addRequestBucket();
        }
        leakyBucketService.leakRequestFromBucket();
        Assertions.assertEquals(5, leakyBucketService.getCurrentWater());
    }
    @Test
    void givenLessLeakRateInBucket_whenLeakRequestFromBucket_thenShouldBucketIsEmpty(){
        for (int i = 0; i < 4; i++) {
            leakyBucketService.addRequestBucket();
        }
        leakyBucketService.leakRequestFromBucket();
        Assertions.assertEquals(0, leakyBucketService.getCurrentWater());
    }

}
