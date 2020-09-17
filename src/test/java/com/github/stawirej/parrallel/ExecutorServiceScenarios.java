package com.github.stawirej.parrallel;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
final class ExecutorServiceScenarios {

    private static final ThreadFactory threadFactory = runnable -> {
        Thread thread = Executors.defaultThreadFactory().newThread(runnable);
        thread.setDaemon(true);
        return thread;
    };

    @Test
    void executor_service() {

        ExecutorService executorService = Executors.newFixedThreadPool(10, threadFactory);

        System.out.println("currentThread = " + Thread.currentThread().getName());

        executorService.submit(() -> {
            Thread currentThread = Thread.currentThread();
            String oldName = currentThread.getName();
            currentThread.setName("Processing-X");
            try {
                Thread.sleep((long) (Math.random() * 1000));
                System.out.println("currentThread = " + Thread.currentThread().getName());
            } catch (Throwable t) {
                t.printStackTrace();
            } finally {
                currentThread.setName(oldName);
            }
        });

        executorService.shutdown();
    }

    @Test
    void executor_service_wait() {

        ExecutorService executorService = Executors.newFixedThreadPool(10, threadFactory);

        System.out.println("currentThread = " + Thread.currentThread().getName());

        executorService.submit(() -> {
            Thread currentThread = Thread.currentThread();
            String oldName = currentThread.getName();
            currentThread.setName("Processing-X");
            try {
                Thread.sleep((long) (Math.random() * 1000));
                System.out.println("currentThread = " + Thread.currentThread().getName());
            } catch (Throwable t) {
                t.printStackTrace();
            } finally {
                currentThread.setName(oldName);
            }
        });

        executorService.shutdown();
        waitForThreads(executorService);
    }

    @Test
    void executor_service_function() {

        ExecutorService executorService = Executors.newFixedThreadPool(10, threadFactory);

        System.out.println("currentThread = " + Thread.currentThread().getName());

        executorService.submit(() -> process());

        executorService.shutdown();
        waitForThreads(executorService);
    }

    @Test
    void executor_service_list_of_tasks() {

        List<Runnable> runnables = List.of(
                () -> process(1),
                () -> process(2),
                () -> process(3),
                () -> process(4),
                () -> process(5),
                () -> process(6)
        );

        ExecutorService executorService = Executors.newFixedThreadPool(10, threadFactory);

        System.out.println("currentThread = " + Thread.currentThread().getName());

        runnables.forEach(executorService::submit);

        executorService.shutdown();
    }

    @Test
    void executor_service_list_of_tasks_wait() {

        List<Runnable> runnables = List.of(
                () -> process(1),
                () -> process(2),
                () -> process(3),
                () -> process(4),
                () -> process(5),
                () -> process(6)
        );

        ExecutorService executorService = Executors.newFixedThreadPool(10, threadFactory);

        System.out.println("currentThread = " + Thread.currentThread().getName());

        runnables.forEach(executorService::submit);

        executorService.shutdown();
        waitForThreads(executorService);
    }

    private void waitForThreads(ExecutorService executorService) {

        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException ex) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private void process() {

        process(0);
    }

    private void process(int id) {

        Thread currentThread = Thread.currentThread();
        String oldName = currentThread.getName();
        currentThread.setName("Processing-" + id);
        try {
            Thread.sleep((long) (Math.random() * 1000));
            System.out.println("currentThread = " + Thread.currentThread().getName());
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            currentThread.setName(oldName);
        }
    }
}
