package com.fiosys.expensor.transactions;

import com.fiosys.expensor.accounts.Account;
import com.fiosys.expensor.category.Category;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by Rehan on 20/9/15.
 */
@DatabaseTable (tableName = "TRANSACTIONS", daoClass = TransactionDAO.class)
public class Transaction {

    @DatabaseField (generatedId = true,columnName = "_id")
    private Long id;
    @DatabaseField
    private Date date;
    @DatabaseField (foreign = true, foreignAutoRefresh = true)
    private Account debitAccount;
    @DatabaseField (foreign = true, foreignAutoRefresh = true)
    private Account creditAccount;
    @DatabaseField
    private Type type;
    @DatabaseField (foreign = true, foreignAutoRefresh = true)
    private Category category;
    @DatabaseField
    private Double amount;

    private boolean transactionProcessed = false;
    private boolean transactionRollbacked = false;

    public Transaction(){}

    public Transaction(Long id, Date date, Account debitAccount, Account creditAccount, Type type, Category category, Double amount) {
        this.id = id;
        this.date = date;
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.debitAccount = debitAccount;
        this.creditAccount = creditAccount;
    }

    public Transaction(Date date, Account debitAccount, Account creditAccount, Type type, Category category, Double amount) {
        this(null, date, debitAccount, creditAccount, type, category, amount);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Account getDebitAccount() {
        return debitAccount;
    }

    public void setDebitAccount(Account debitAccount) {
        if(type != Type.INCOME) this.debitAccount = debitAccount;
    }

    public Account getCreditAccount() {
        return creditAccount;
    }

    public void setCreditAccount(Account creditAccount) {
        if(type!=Type.EXPENSE) this.creditAccount = creditAccount;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public enum Type{
        INCOME("INCOME"),
        EXPENSE("EXPENSE"),
        TRANSFER("TRANSFER");

        private String type;

        Type(String type){
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return type;
        }

        public static Type fromString(String type){

            if (type != null) {
                for (Type t : Type.values()) {
                    if (type.equalsIgnoreCase(t.type)) {
                        return t;
                    }
                }
            }
            return null;

        }

        public static String[] names() {
            Type[] types = values();
            String[] names = new String[types.length];

            for (int i = 0; i < types.length; i++) {
                names[i] = types[i].name();
            }

            return names;
        }
    }

    public void processTransaction(){

        if(transactionProcessed) return;

        if(type == Type.EXPENSE || type == Type.TRANSFER){
            debitAccount.debitBalance(amount);
        }
        if(type == Type.INCOME || type == Type.TRANSFER){
            creditAccount.creditBalance(amount);
        }

        transactionProcessed = true;
        transactionRollbacked = false;

    }

    public void rollbackTransaction(){

        if(transactionRollbacked) return;

        if(type == Type.EXPENSE || type == Type.TRANSFER){
            debitAccount.creditBalance(amount);
        }

        if(type == Type.INCOME || type == Type.TRANSFER){
            creditAccount.debitBalance(amount);
        }
        transactionRollbacked = true;
        transactionProcessed = false;
    }


}
