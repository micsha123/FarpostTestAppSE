package ru.farpost.dmitry.farposttestappse.model;

import java.util.ArrayList;

public class EventResult {

    public ArrayList<Repository> getItems() {
        return items;
    }

    private ArrayList<Repository> items;

    public EventResult(ArrayList<Repository> items) {
        this.items = items;
    }

}
