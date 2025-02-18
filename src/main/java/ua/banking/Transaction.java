package ua.banking;

/**
 * Клас, що представляє транзакцію
 */
public class Transaction {
    private final int fromId;
    private final int toId;
    private final double amount;
    private final long timestamp;

    /**
     * Конструктор транзакції
     *
     * @param fromId ідентифікатор рахунку відправника
     * @param toId ідентифікатор рахунку одержувача
     * @param amount сума переказу
     * @param timestamp час здійснення транзакції
     *
     * @requires amount > 0
     * @requires fromId != toId
     * @ensures this.fromId == fromId && this.toId == toId && this.amount == amount && this.timestamp == timestamp
     */
    public Transaction(int fromId, int toId, double amount, long timestamp) {
        this.fromId = fromId;
        this.toId = toId;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    /**
     * Отримання ідентифікатора рахунку відправника
     *
     * @return ідентифікатор рахунку відправника
     */
    public int getFromId() {
        return fromId;
    }

    /**
     * Отримання ідентифікатора рахунку одержувача
     *
     * @return ідентифікатор рахунку одержувача
     */
    public int getToId() {
        return toId;
    }

    /**
     * Отримання суми переказу
     *
     * @return сума переказу
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Отримання часу здійснення транзакції
     *
     * @return час здійснення транзакції
     */
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("Транзакція: з рахунку %d на рахунок %d, сума: %.2f, час: %d",
                              fromId, toId, amount, timestamp);
    }
}