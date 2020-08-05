package task_6;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TaskTest {
    private Task testClass;

    @BeforeEach
    public void init() {
        testClass = new Task();
    }

    @Test
    public void testOneOperation1() {
        int[] source = {1, 2, 4, 2, 3};
        int[] result = {2, 3};
        Assertions.assertArrayEquals(result, testClass.doTask1(source));
    }

    @Test
    public void testOneOperation2() {
        int[] source = {1, 2, 4, 2, 3, 4, 2};
        int[] result = {2};
        Assertions.assertArrayEquals(result, testClass.doTask1(source));
    }

    @Test
    public void testOneOperation3() {
        int[] source = {1, 2, 4, 2, 3, 4, 4, 3, 2};
        int[] result = {3, 2};
        Assertions.assertArrayEquals(result, testClass.doTask1(source));
    }

    @Test
    public void testOneOperation4() {
        int[] source = {1, 2, 4, 2, 3 ,5, 6};
        int[] result = {2, 3, 5 };
        Assertions.assertArrayEquals(result, testClass.doTask1(source));
    }

    @Test
    public void testTwoOperation1(){
        int[] source = {1, 2, 4, 2, 3 };
         Assertions.assertTrue(testClass.doTask2(source));
    }

    @Test
    public void testTwoOperation2(){
        int[] source = {1, 4, 4, 1, 1 };
        Assertions.assertTrue(testClass.doTask2(source));
    }

    @Test
    public void testTwoOperation3(){
        int[] source = {1, 4, 4, 4, 4 };
        Assertions.assertTrue(testClass.doTask2(source));
    }

    @Test
    public void testTwoOperation4(){
        int[] source = {4, 4, 4};
        Assertions.assertTrue(testClass.doTask2(source));
    }
}