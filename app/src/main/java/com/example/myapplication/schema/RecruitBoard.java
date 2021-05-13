package com.example.myapplication.schema;

import android.app.Activity;
import android.widget.Toast;

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
    private ArrayList<String> userIdList;

    private String chatUserId1;
    private String chatUserId2;
    private String chatUserId3;
    private String chatUserId4;

    // 아직 주소데이터를 넣지 않음
    // clicks 넣어야 하나?
    // clickCount 는 마지막에 개발@@@@@@@@@@@
    public RecruitBoard(String userIdRecruit, String nameRecruit, String titleRecruit, String contentRecruit,
                        long createRecruit, String recruitMonth, String recruitDay,
                        int totalMeetingNumber, int nowMeetingNumber,
                        String chatUserId1) {
        this.userIdRecruit      = userIdRecruit;
        this.nameRecruit        = nameRecruit;
        this.titleRecruit       = titleRecruit;
        this.contentRecruit     = contentRecruit;
        this.createRecruit      = createRecruit;
        this.recruitMonth       = recruitMonth;
        this.recruitDay         = recruitDay;
        this.totalMeetingNumber = totalMeetingNumber;
        this.nowMeetingNumber   = nowMeetingNumber;
        this.chatUserId1        = chatUserId1;
    }

    public RecruitBoard(String userIdRecruit, String nameRecruit, String titleRecruit, String contentRecruit,
                        long createRecruit, String recruitMonth, String recruitDay,
                        int totalMeetingNumber, int nowMeetingNumber,
                        String chatUserId1, String chatUserId2) {
        this.userIdRecruit      = userIdRecruit;
        this.nameRecruit        = nameRecruit;
        this.titleRecruit       = titleRecruit;
        this.contentRecruit     = contentRecruit;
        this.createRecruit      = createRecruit;
        this.recruitMonth       = recruitMonth;
        this.recruitDay         = recruitDay;
        this.totalMeetingNumber = totalMeetingNumber;
        this.nowMeetingNumber   = nowMeetingNumber;
        this.chatUserId1        = chatUserId1;
        this.chatUserId2        = chatUserId2;
    }

    public RecruitBoard(String userIdRecruit, String nameRecruit, String titleRecruit, String contentRecruit,
                        long createRecruit, String recruitMonth, String recruitDay,
                        int totalMeetingNumber, int nowMeetingNumber,
                        String chatUserId1, String chatUserId2, String chatUserId3) {
        this.userIdRecruit      = userIdRecruit;
        this.nameRecruit        = nameRecruit;
        this.titleRecruit       = titleRecruit;
        this.contentRecruit     = contentRecruit;
        this.createRecruit      = createRecruit;
        this.recruitMonth       = recruitMonth;
        this.recruitDay         = recruitDay;
        this.totalMeetingNumber = totalMeetingNumber;
        this.nowMeetingNumber   = nowMeetingNumber;
        this.chatUserId1        = chatUserId1;
        this.chatUserId2        = chatUserId2;
        this.chatUserId3        = chatUserId3;
    }

    public RecruitBoard(String userIdRecruit, String nameRecruit, String titleRecruit, String contentRecruit,
                        long createRecruit, String recruitMonth, String recruitDay,
                        int totalMeetingNumber, int nowMeetingNumber,
                        String chatUserId1, String chatUserId2, String chatUserId3, String chatUserId4) {
        this.userIdRecruit      = userIdRecruit;
        this.nameRecruit        = nameRecruit;
        this.titleRecruit       = titleRecruit;
        this.contentRecruit     = contentRecruit;
        this.createRecruit      = createRecruit;
        this.recruitMonth       = recruitMonth;
        this.recruitDay         = recruitDay;
        this.totalMeetingNumber = totalMeetingNumber;
        this.nowMeetingNumber   = nowMeetingNumber;
        this.chatUserId1        = chatUserId1;
        this.chatUserId2        = chatUserId2;
        this.chatUserId3        = chatUserId3;
        this.chatUserId4        = chatUserId4;
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

    public String getChatUserId1() {
        return chatUserId1;
    }

    public String getChatUserId2() {
        return chatUserId2;
    }

    public void setChatUserId2(String chatUserId2) {
        this.chatUserId2 = chatUserId2;
    }

    public String getChatUserId3() {
        return chatUserId3;
    }

    public void setChatUserId3(String chatUserId3) {
        this.chatUserId3 = chatUserId3;
    }

    public String getChatUserId4() {
        return chatUserId4;
    }

    public void setChatUserId4(String chatUserId4) {
        this.chatUserId4 = chatUserId4;
    }


    /**
     * chatRoom: contentId(문서 id)
     * userId
     * user profile
     * userName
     * message
     * create
     */
    public static class Chat implements Serializable{
        private String chatRoom;
        private String userChatId;
        private String profileUrl;
        private String chatName;
        private String message;
        private long chatCreate;

        public Chat(String chatRoom, String userChatId, String profileUrl,
                    String chatName, String message, long chatCreate) {
            this.chatRoom = chatRoom;
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

        public String getChatRoom() {
            return chatRoom;
        }
        public void setChatRoom(String chatRoom) {
            this.chatRoom = chatRoom;
        }
    }
}
