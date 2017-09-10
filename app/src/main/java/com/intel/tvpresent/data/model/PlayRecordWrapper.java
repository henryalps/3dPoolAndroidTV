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

    public String getScore() {
        return playRecorder.getString("score");
    }

    public String getLuckyBall() {
        return playRecorder.getString("luckyBallCount");
    }

    public String getStarBag() {
        return playRecorder.getString("starBagCount");
    }

    public String getMaxCombo() {
        JSONObject combo = playRecorder.getJSONObject("combo");
        String maxCombo = "0";
        for (int i = 0; i < 20; i++) {
            try {
                combo.get(String.valueOf(i));
                maxCombo = String.valueOf(i);
            } catch (Exception ex){}
        }
        return maxCombo;
    }

    public String getMaxMultiPot() {
        JSONObject combo = playRecorder.getJSONObject("multiPot");
        String maxCombo = "0";
        for (int i = 0; i < 20; i++) {
            try {
                combo.get(String.valueOf(i));
                maxCombo = String.valueOf(i);
            } catch (Exception ex){}
        }
        return maxCombo;
    }
}
