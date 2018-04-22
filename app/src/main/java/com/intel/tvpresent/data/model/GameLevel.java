package com.intel.tvpresent.data.model;

/**
 * Created by henryalps on 2017/9/10.
 */

public class GameLevel {

    private String name;

    private String id;

    private String thumb;

    private int order;

    public GameLevel(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}