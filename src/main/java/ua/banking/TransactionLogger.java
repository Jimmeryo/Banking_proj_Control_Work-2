package ua.banking;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Клас для логування транзакцій
 */
public class TransactionLogger {
    private final Queue<Transaction> transactionLog;

    /**
     * Конструктор логгера транзакцій
     *
     * @ensures transactionLog != null
     */
    public TransactionLogger() {
        this.transactionLog = new ConcurrentLinkedQueue<>();
    }

    /**
     * Логування нової транзакції
     *
     * @param fromId ідентифікатор рахунку відправника
     * @param toId ідентифікатор рахунку одержувача
     * @param amount сума переказу
     *
     * @requires amount > 0
     * @requires fromId != toId
     * @ensures transactionLog.size() == \old(transactionLog.size()) + 1
     */
    public void logTransaction(int fromId, int toId, double amount) {
        Transaction transaction = new Transaction(fromId, toId, amount, System.currentTimeMillis());
        transactionLog.offer(transaction);
    }

    /**
     * Отримання історії транзакцій
     *
     * @return список транзакцій
     * @ensures \result != null
     */
    public List<Transaction> getTransactionHistory() {
        return new ArrayList<>(transactionLog);
    }
}