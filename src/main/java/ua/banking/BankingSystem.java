package ua.banking;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Головний клас банківської системи
 */
public class BankingSystem {

    /**
     * Головний метод для демонстрації роботи системи
     * @param args аргументи командного рядка
     */
    public static void main(String[] args) {
        TransactionManager manager = new TransactionManager();
        ExecutorService executor = Executors.newFixedThreadPool(5); // Створюємо ExecutorService поза try
        try {
            // Створюємо CountDownLatch для синхронізації завершення всіх транзакцій
            CountDownLatch latch = new CountDownLatch(10);

            // Створюємо кілька паралельних транзакцій
            for (int i = 0; i < 10; i++) {
                executor.submit(() -> {
                    try {
                        // Випадковий переказ між рахунками
                        boolean success = manager.processTransfer(1, 2, 100.0);
                        System.out.println("Результат транзакції: " + success);
                    } finally {
                        latch.countDown();
                    }
                });
            }

            // Очікуємо завершення всіх транзакцій
            boolean allTransactionsCompleted = false;
            try {
                allTransactionsCompleted = latch.await(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Очікування транзакцій було перервано");
            }

            if (!allTransactionsCompleted) {
                System.err.println("Не всі транзакції завершилися вчасно");
            }

        } finally { // Замість try-with-resources використовуємо finally для закриття ресурсу
            // Ініціюємо завершення executor
            executor.shutdown();

            // Очікуємо завершення всіх задач або таймауту
            try {
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    System.err.println("Деякі задачі не завершилися вчасно");
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Виконання було перервано при очікуванні завершення ExecutorService");
                executor.shutdownNow(); // Важливо закрити навіть якщо перервано
            }
        }

        // Виводимо інформацію про рахунки
        System.out.println("Баланс рахунку 1: " + manager.getAccount(1).getBalance());
        System.out.println("Баланс рахунку 2: " + manager.getAccount(2).getBalance());

        // Виводимо історію транзакцій
        System.out.println("\nІсторія транзакцій:");
        manager.getTransactionLogger().getTransactionHistory()
               .forEach(System.out::println);
    }
}