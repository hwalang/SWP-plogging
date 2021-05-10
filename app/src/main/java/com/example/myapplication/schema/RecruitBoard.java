package com.example.myapplication.schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecruitBoard implements Serializable {
    /**
     * 계정 id
     * 작성자
     * 제목
     * 내용
     * 작성일
     * 작성자 주소
     * 약속 날짜
     * 클릭수
     * 모임인원
     */
    private String userIdRecruit;
    private String nameRecruit;
    private String titleRecruit;
    private String contentRecruit;
    private long createRecruit;
    private String address;
    private String recruitMonth;
    private String recruitDay;
    private final int clickCount = 0;
    private final Map<String, Boolean> clicks = new HashMap<>();
    private int totalMeetingNumber;
    private int nowMeetingNumber;

    // 아직 주소데이터를 넣지 않음
    // clicks 넣어야 하나?
    // clickCount 는 마지막에 개발@@@@@@@@@@@
    public RecruitBoard(String userIdRecruit, String nameRecruit, String titleRecruit, String contentRecruit,
                        long createRecruit, String recruitMonth, String recruitDay,
                        int totalMeetingNumber, int nowMeetingNumber) {
        this.userIdRecruit      = userIdRecruit;
        this.nameRecruit        = nameRecruit;
        this.titleRecruit       = titleRecruit;
        this.contentRecruit     = contentRecruit;
        this.createRecruit      = createRecruit;
        this.recruitMonth       = recruitMonth;
        this.recruitDay         = recruitDay;
        this.totalMeetingNumber = totalMeetingNumber;
        this.nowMeetingNumber   = nowMeetingNumber;
    }

    public String getUserIdRecruit() {
        return userIdRecruit;
    }

    public String getNameRecruit() {
        return nameRecruit;
    }

    public String getTitleRecruit() {
        return titleRecruit;
    }

    public String getContentRecruit() {
        return contentRecruit;
    }

    public long getCreateRecruit() {
        return createRecruit;
    }

    public String getRecruitMonth() {
        return recruitMonth;
    }

    public String getRecruitDay() {
        return recruitDay;
    }

    public int getClickCount() {
        return clickCount;
    }

    public int getTotalMeetingNumber() {
        return totalMeetingNumber;
    }

    public int getNowMeetingNumber() {
        return nowMeetingNumber;
    }

    public boolean isDeadlineCheck() {
        return false;
    }

    // 현재 모집원 수 증가
    public void setNowMeetingNumber(int nowMeetingNumber) {
        this.nowMeetingNumber = nowMeetingNumber;
    }


    /**
     * userId
     * user profile
     * userName
     * message
     * create
     */
    public static class Chat implements Serializable{
        private String userChatId;
        private String profileUrl;
        private String chatName;
        private String message;
        private long chatCreate;

        public Chat(String userChatId, String profileUrl, String chatName,
                    String message, long chatCreate) {
            this.userChatId = userChatId;
            this.profileUrl = profileUrl;
            this.chatName = chatName;
            this.message = message;
            this.chatCreate = chatCreate;
        }

        public String getProfileUrl() {
            return profileUrl;
        }

        public String getMessage() {
            return message;
        }

        public long getChatCreate() {
            return chatCreate;
        }

        public String getUserChatId() {
            return userChatId;
        }

        public String getChatName() {
            return chatName;
        }
    }
}
