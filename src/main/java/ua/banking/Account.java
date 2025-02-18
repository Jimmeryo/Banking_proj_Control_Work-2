package ua.banking;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Клас рахунку з реалізацією логіки Флойда-Хоара
 *
 * @invariant balance >= 0 (баланс завжди невід'ємний)
 * @invariant lock != null (блокування завжди ініціалізоване)
 */
public class Account {
    private int id;
    private volatile double balance;
    private final ReentrantLock lock;

    /**
     * Конструктор для створення нового рахунку
     *
     * @param id ідентифікатор рахунку
     * @param initialBalance початковий баланс
     * @requires initialBalance >= 0
     * @ensures this.id == id && this.balance == initialBalance && this.lock != null
     */
    public Account(int id, double initialBalance) {
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Початковий баланс не може бути від'ємним");
        }
        this.id = id;
        this.balance = initialBalance;
        this.lock = new ReentrantLock();
    }

    /**
     * Метод для безпечного переказу коштів з застосуванням логіки Флойда-Хоара
     *
     * @param target цільовий рахунок для переказу
     * @param amount сума переказу
     * @return успішність операції
     *
     * @requires this != target (не можна переказувати самому собі)
     * @requires amount > 0 (сума переказу має бути позитивною)
     * @requires this.balance >= amount (на рахунку має бути достатньо коштів)
     * @ensures \result == true ==> this.balance == \old(this.balance) - amount
     * @ensures \result == true ==> target.balance == \old(target.balance) + amount
     * @ensures \result == false ==> this.balance == \old(this.balance)
     * @ensures \result == false ==> target.balance == \old(target.balance)
     */
    public boolean transfer(Account target, double amount) {
        // Перевірка передумов
        if (this == target) return false; // Не можна переказувати самому собі
        if (amount <= 0) return false;    // Сума має бути позитивною

        // Впорядкування ресурсів для запобігання взаємним блокуванням (метод Гріс-Овіцкі)
        Account first = this.id < target.id ? this : target;
        Account second = this.id < target.id ? target : this;

        try {
            // Спроба заблокувати перший рахунок з таймаутом
            if (first.lock.tryLock(1000, TimeUnit.MILLISECONDS)) {
                try {
                    // Спроба заблокувати другий рахунок з таймаутом
                    if (second.lock.tryLock(1000, TimeUnit.MILLISECONDS)) {
                        try {
                            // Перевірка достатності коштів
                            if (this.balance >= amount) {
                                // Атомарна операція переказу
                                this.balance -= amount;
                                target.balance += amount;
                                return true;
                            }
                        } finally {
                            // Гарантоване розблокування другого рахунку
                            second.lock.unlock();
                        }
                    }
                } finally {
                    // Гарантоване розблокування першого рахунку
                    first.lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return false;
    }

    /**
     * Отримання поточного балансу рахунку
     *
     * @return поточний баланс
     * @ensures \result == this.balance
     */
    public double getBalance() {
        lock.lock();
        try {
            return balance;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Отримання ідентифікатора рахунку
     *
     * @return ідентифікатор рахунку
     * @ensures \result == this.id
     */
    public int getId() {
        return id;
    }
}