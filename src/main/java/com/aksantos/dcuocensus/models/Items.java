package com.aksantos.dcuocensus.models;

import java.util.ArrayList;
import java.util.List;

public class Items {
    private List<Item> item_list;
    private long returned;

    public Items() {
        item_list = new ArrayList<Item>();
    }

    public List<Item> getItem_list() {
        return item_list;
    }

    public void setItem_list(List<Item> item_list) {
        this.item_list = item_list;
    }

    public long getReturned() {
        return returned;
    }

    public void setReturned(long returned) {
        this.returned = returned;
    }

}
