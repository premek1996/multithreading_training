package example;

import java.util.concurrent.Executors;

public class ThreadExample2 {

    public static void main(String[] args) {
        var executorService = Executors.newFixedThreadPool(2);
        executorService.submit(ThreadExample2::printNumbers);
        executorService.submit(ThreadExample2::printNumbers);
        executorService.shutdown();
    }

    private static void printNumbers() {
        for (int i = 0; i < 1000; i++) {
            System.out.println(Thread.currentThread().getName() + " " + i);
        }
    }

}
