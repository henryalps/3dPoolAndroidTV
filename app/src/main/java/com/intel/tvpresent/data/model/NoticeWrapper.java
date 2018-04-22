package com.intel.tvpresent.data.model;

import com.alibaba.fastjson.JSONObject;

public class NoticeWrapper {
    private JSONObject notice;
    private String title = "";
    private String content = "";

    public NoticeWrapper(JSONObject notice) {
        this.notice = notice;
        try {
            this.title = notice.getString("title");
            this.content = notice.getString("content");
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public JSONObject getNotice() {
        return notice;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}