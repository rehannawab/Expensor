package com.fiosys.expensor.accounts;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by root on 20/9/15.
 */
@DatabaseTable (tableName = "ACCOUNT", daoClass = AccountDAO.class)
public class Account {

    @DatabaseField(generatedId = true, columnName = "_id")
    Long id;
    @DatabaseField (unique = true)
    String name;
    @DatabaseField
    Double balance;

    public Account(){}

    public Account(Long id, String name, Double balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    public Account(String name, Double balance) {
        this.name = name;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    private void updateBalance(Double value){

        setBalance(getBalance() + value);

    }

    public void creditBalance(Double value){
        updateBalance(value);
    }

    public void debitBalance(Double value){
        updateBalance(-value);
    }
}
