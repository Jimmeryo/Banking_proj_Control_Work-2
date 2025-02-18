package ua.banking;

import ua.banking.TransactionManager;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        TransactionManager manager = new TransactionManager();
        Scanner scanner = new Scanner(System.in); // Створюємо Scanner для введення даних з консолі

        // Створюємо початкові рахунки
        manager.createAccount(1, 1000.0);
        manager.createAccount(2, 500.0);
        manager.createAccount(3, 2000.0);

        System.out.println("Початкові баланси рахунків:");
        System.out.println("Рахунок 1: " + manager.getAccount(1).getBalance());
        System.out.println("Рахунок 2: " + manager.getAccount(2).getBalance());
        System.out.println("Рахунок 3: " + manager.getAccount(3).getBalance());
        System.out.println("--------------------");

        while (true) { // Запускаємо безкінечний цикл для введення транзакцій
            System.out.println("\nВведіть дані транзакції:");

            System.out.print("З рахунку (ID відправника): ");
            int fromAccountId = scanner.nextInt(); // Зчитуємо ID рахунку відправника
            System.out.print("На рахунок (ID отримувача): ");
            int toAccountId = scanner.nextInt();   // Зчитуємо ID рахунку отримувача
            System.out.print("Сума переказу: ");
            double amount = scanner.nextDouble();  // Зчитуємо суму переказу

            boolean success = manager.processTransfer(fromAccountId, toAccountId, amount); // Обробляємо транзакцію
            System.out.println("Результат транзакції: " + (success ? "успішна" : "невдала")); // Виводимо результат

            System.out.print("Виконати ще одну транзакцію? (так/ні): ");
            String continueTransaction = scanner.next(); // Питаємо, чи хоче користувач виконати ще одну транзакцію
            if (!continueTransaction.equalsIgnoreCase("так")) { // Якщо відповідь не "так" (наприклад, "ні", "n", "exit" і т.д.)
                break; // Виходимо з циклу
            }
        }

        System.out.println("--------------------");
        System.out.println("Баланси рахунків після транзакцій:");
        System.out.println("Рахунок 1: " + manager.getAccount(1).getBalance());
        System.out.println("Рахунок 2: " + manager.getAccount(2).getBalance());
        System.out.println("Рахунок 3: " + manager.getAccount(3).getBalance());

        System.out.println("--------------------");
        System.out.println("Історія транзакцій:");
        manager.getTransactionLogger().getTransactionHistory().forEach(System.out::println); // Виводимо історію транзакцій

        scanner.close(); // Закриваємо Scanner, щоб звільнити ресурси
    }
}