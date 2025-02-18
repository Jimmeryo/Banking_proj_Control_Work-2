package ua.banking;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Клас для управління транзакціями
 * @invariant (∀ a, b ∈ accounts: a.id != b.id) (унікальність ідентифікаторів)
 * @invariant ∀ account ∈ accounts: account.balance >= 0 (невід'ємний баланс)
 */
public class TransactionManager {
    private final Map<Integer, Account> accounts;
    private final TransactionLogger logger;

    /**
     * Конструктор менеджера транзакцій
     *
     * @ensures accounts != null && logger != null
     */
    public TransactionManager() {
        this.accounts = new ConcurrentHashMap<>();
        this.logger = new TransactionLogger();
    }

    /**
     * Створення нового рахунку
     *
     * @param id ідентифікатор рахунку
     * @param initialBalance початковий баланс
     *
     * @requires id ∉ accounts.keySet() (ідентифікатор має бути унікальним)
     * @requires initialBalance >= 0 (початковий баланс невід'ємний)
     * @ensures accounts.containsKey(id)
     * @ensures accounts.get(id).getBalance() == initialBalance
     */
    public void createAccount(int id, double initialBalance) {
    System.out.println("Створення рахунку з ID: " + id + ", початковий баланс: " + initialBalance); // Доданий вивід на початку

    if (accounts.containsKey(id)) {
        throw new IllegalArgumentException("Рахунок з таким ідентифікатором вже існує: " + id);
    }
    if (initialBalance < 0) {
        throw new IllegalArgumentException("Початковий баланс не може бути від'ємним");
    }
    accounts.put(id, new Account(id, initialBalance));

    System.out.println("Рахунок з ID: " + id + " створено успішно."); // Доданий вивід в кінці
    }

    /**
     * Обробка переказу між рахунками
     *
     * @param fromId ідентифікатор рахунку відправника
     * @param toId ідентифікатор рахунку одержувача
     * @param amount сума переказу
     * @return успішність операції
     *
     * @requires fromId ∈ accounts.keySet() (рахунок відправника має існувати)
     * @requires toId ∈ accounts.keySet() (рахунок одержувача має існувати)
     * @requires fromId != toId (не можна переказувати самому собі)
     * @requires amount > 0 (сума переказу має бути позитивною)
     * @ensures \result == true ==> accounts.get(fromId).getBalance() == \old(accounts.get(fromId).getBalance()) - amount
     * @ensures \result == true ==> accounts.get(toId).getBalance() == \old(accounts.get(toId).getBalance()) + amount
     * @ensures \result == false ==> accounts.get(fromId).getBalance() == \old(accounts.get(fromId).getBalance())
     * @ensures \result == false ==> accounts.get(toId).getBalance() == \old(accounts.get(toId).getBalance())
     */
    public boolean processTransfer(int fromId, int toId, double amount) {
        Account from = accounts.get(fromId);
        Account to = accounts.get(toId);

        // Перевірка передумов
        if (from == null || to == null || amount <= 0) {
            return false;
        }

        // Виконання переказу
        boolean success = from.transfer(to, amount);
        if (success) {
            logger.logTransaction(fromId, toId, amount);
        }
        return success;
    }

    /**
     * Отримання рахунку за ідентифікатором
     *
     * @param id ідентифікатор рахунку
     * @return рахунок або null, якщо не знайдено
     *
     * @ensures \result != null ==> \result == accounts.get(id)
     */
    public Account getAccount(int id) {
        return accounts.get(id);
    }

    /**
     * Отримання логгера транзакцій
     *
     * @return логгер транзакцій
     * @ensures \result == logger
     */
    public TransactionLogger getTransactionLogger() {
        return logger;
    }
}