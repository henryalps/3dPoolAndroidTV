package com.intel.tvpresent.data.model;

import com.alibaba.fastjson.JSONObject;

public class PlayRecordWrapper {
    private JSONObject playRecorder;

    public PlayRecordWrapper(JSONObject playRecorder) {
        this.playRecorder = playRecorder;
    }

    public String getVideoUrl() {
        return playRecorder.getString("videoUrl");
    }
}
