package com.intel.tvpresent.data.model;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by henryalps on 2018/4/22.
 */

public class ActivityWrapper {
    private String name = "";
    private String startTime = "";
    private String endTime = "";
    private String timeRange = "";

    public ActivityWrapper(JSONObject jsonObject) {
        name = jsonObject.getString("abbreviate");
        startTime = jsonObject.getString("beginTime");
        endTime = jsonObject.getString("endTime");
    }

    public String getTimeRange() {
        return String.format("%s ~ %s", startTime.substring(0, 10), endTime.substring(0, 10));
    }

    public String getName() {
        return name;
    }
}
