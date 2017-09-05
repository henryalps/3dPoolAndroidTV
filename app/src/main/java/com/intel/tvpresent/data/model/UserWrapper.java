package com.intel.tvpresent.data.model;

import com.alibaba.fastjson.JSONObject;

public class UserWrapper {
    private JSONObject user;
    private PlayRecordWrapper playRecordWrapper;
    private String id;
    private int rank;

    public UserWrapper(JSONObject user) {
        this.id = user.getString("idRecord");
        this.rank = user.getIntValue("rank");
        this.user = user.getJSONObject("user");
        this.playRecordWrapper = new PlayRecordWrapper(user.getJSONObject("summary"));
    }

    public String getAvatarUrl() {
        return user.getString("avatarUrl");
    }

    public String getNickName() {
        return user.getString("nickName");
    }

    public int getGender() {
        if ("male".equals(user.getString("gender"))) {
            return 0;
        } else if ("female".equals(user.getString("gender"))) {
            return 1;
        } else return -1;
    }

    public PlayRecordWrapper getPlayRecordWrapper() {
        return playRecordWrapper;
    }

    public String getId() {
        return id;
    }

    public int getRank() {
        return rank;
    }
}