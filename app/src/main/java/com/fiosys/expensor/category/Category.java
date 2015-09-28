package com.fiosys.expensor.category;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Rehan on 18/9/15.
 */
@DatabaseTable (tableName = "CATEGORY", daoClass = CategoryDAO.class)
public class Category {

    @DatabaseField (generatedId = true, columnName = "_id")
    private Long id;
    @DatabaseField (uniqueCombo = true)
    private  String type;
    @DatabaseField (uniqueCombo = true)
    private String name;

    public Category(){}

    public Category(String name, String type){
        this(0l,name,type);
    }

    Category(Long id, String name, String type){
        this.id = id;
        this.type = type;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
