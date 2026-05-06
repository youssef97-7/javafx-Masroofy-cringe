package com.masroofy.model;

/**
 * Represents a classification for financial transactions.
 * Used to group expenses into specific types like food, transport, or entertainment.
 */
public class Category {
    private String type;

    /**
     * Constructs a new Category with a specified label.
     *
     * @param type The name or label for the category.
     */
    public Category(String type){
        this.type = type;
    }

    /**
     * Retrieves the name of the category.
     *
     * @return The current type label.
     */
    public String getType(){
        return this.type;
    }

    /**
     * Updates the name of the category.
     *
     * @param type The new name to assign to this category.
     */
    public void setType(String type){
        this.type = type;
    }
}
