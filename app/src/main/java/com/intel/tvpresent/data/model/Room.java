package com.intel.tvpresent.data.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by henryalps on 2017/8/27.
 */

@Singleton
public class Room {
    private int MAX_USER_SIZE = 10;
    private String rawJson = " ";
    private String notification = " ";
    private String notice = " ";
    private String gameStatement = " ";
    private String qrcodeUrl = "";
    private Map<GameLevel, List<UserWrapper>> userWrapperList;

    public String getRawJson() {
        return rawJson;
    }

    @Inject
    public Room() {
    }

    public boolean updateInPush(String rawJson) {
        System.out.println(String.format("RAWJSON:PUSH:%s", rawJson));
        try {
            if (!getRawJson().equals(rawJson)) {
                this.rawJson = rawJson;
                setUserWrapperList(parseRawJson(rawJson));
                return true;
            }
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private String jsonToStr(JSONObject jsonObject) {
        NoticeWrapper notice = new NoticeWrapper(jsonObject);
        return String.format("【%s】\n%s", notice.getTitle(), notice.getContent());
    }

    public boolean updateInLogin(String rawJson) {
        System.out.println(String.format("RAWJSON:LOGIN:%s", rawJson));
        try {
            JSONObject jsonObject = JSON.parseObject(rawJson).getJSONObject("data");
            this.rawJson = jsonObject.getString("ranklist");
            this.notice =  jsonToStr(jsonObject.getJSONObject("notice"));
            this.gameStatement = jsonToStr(jsonObject.getJSONObject("gameStatement"));
            this.qrcodeUrl = jsonObject.getString("deviceQRCode");
            setUserWrapperList(parseRawJson(this.rawJson));
            this.notification = jsonObject.getString("notification");
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public Map<GameLevel, List<UserWrapper>> getUserWrapperList() {
        return userWrapperList;
    }

    public void setUserWrapperList(Map<GameLevel, List<UserWrapper>> userWrapperList) {
        this.userWrapperList = userWrapperList;
    }

    private Map<GameLevel, List<UserWrapper>> parseRawJson(String json) {
        Map<GameLevel, List<UserWrapper>> res = new HashMap<>();
        ArrayList<GameLevel> levels = new ArrayList<>();
        try {
            JSONObject room = JSON.parseObject(json);
            JSONArray levelJson = room.getJSONArray("levelList");
            for (int i=0; i<levelJson.size(); i++) {
                GameLevel gameLevel = new GameLevel();
                gameLevel.setId(levelJson.getJSONObject(i).getString("id"));
                gameLevel.setName("趣桌球公开赛\n" + levelJson.getJSONObject(i).getString("name") + "得分榜");
                gameLevel.setThumb(levelJson.getJSONObject(i).getString("thumbnailUrl"));
                levels.add(gameLevel);
            }

            JSONObject usersInRoom = room.getJSONObject("ranklistMap");
            for (GameLevel level:levels) {
                JSONArray usersInRoomWithLevel = usersInRoom.getJSONArray(level.getId());
                ArrayList<UserWrapper> userList = new ArrayList<UserWrapper>();

                for (int i=0; i<usersInRoomWithLevel.size(); i++) {
                    JSONObject user = usersInRoomWithLevel.getJSONObject(i);
                    userList.add(new UserWrapper(user));
                }
                // sort
                Collections.sort(userList, new Comparator<UserWrapper>() {
                    @Override
                    public int compare(UserWrapper lhs, UserWrapper rhs) {
                        return lhs.getRank() - rhs.getRank();
                    }
                });

                // limit user count in each room
                int userCount = usersInRoomWithLevel.size() > MAX_USER_SIZE ? MAX_USER_SIZE : usersInRoomWithLevel.size();
                userList = new ArrayList<>(userList.subList(0, userCount));
                res.put(level, userList);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return res;
    }

    public String getNotification() {
        return notification;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getGameStatement() {
        return gameStatement;
    }

    public void setGameStatement(String gameStatement) {
        this.gameStatement = gameStatement;
    }

    public String getQrcodeUrl() {
        return qrcodeUrl;
    }

    public void setQrcodeUrl(String qrcodeUrl) {
        this.qrcodeUrl = qrcodeUrl;
    }
}
