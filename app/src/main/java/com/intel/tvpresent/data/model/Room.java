package com.intel.tvpresent.data.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by henryalps on 2017/8/27.
 */

@Singleton
public class Room {
    private String rawJson = "";
    private ArrayList<UserWrapper> userWrapperList;

    public String getRawJson() {
        return rawJson;
    }

    @Inject
    public Room() {
    }

    public boolean update(String rawJson) {
        if (!this.rawJson.equals(rawJson)) {
            this.rawJson = rawJson;
            setUserWrapperList(parseRawJson(rawJson));
            return true;
        }
        return false;
    }

    public ArrayList<UserWrapper> getUserWrapperList() {
        return userWrapperList;
    }

    public void setUserWrapperList(ArrayList<UserWrapper> userWrapperList) {
        this.userWrapperList = userWrapperList;
    }

    private ArrayList<UserWrapper> parseRawJson(String json) {
        ArrayList<UserWrapper> res = new ArrayList<>();
        try {
            JSONObject room = JSON.parseObject(json);
            JSONArray usersInRoom = room.getJSONArray("room");
            for (int i=0; i<usersInRoom.size(); i++) {
                JSONObject user = usersInRoom.getJSONObject(i);
                res.add(new UserWrapper(user));
            }
            // sort
            Collections.sort(res, new Comparator<UserWrapper>() {
                @Override
                public int compare(UserWrapper lhs, UserWrapper rhs) {
                    return lhs.getRank() - rhs.getRank();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return res;
    }
}
