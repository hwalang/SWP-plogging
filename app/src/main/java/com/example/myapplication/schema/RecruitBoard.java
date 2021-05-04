package com.example.myapplication.schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecruitBoard {
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
     * 마감 여부
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
    private int meetingNumber;

    // 아직 주소데이터를 넣지 않음
    // clicks 넣어야 하나?
    // clickCount 는 마지막에 개발@@@@@@@@@@@
    public RecruitBoard(String userIdRecruit, String nameRecruit, String titleRecruit, String contentRecruit,
                        long createRecruit, String recruitMonth, String recruitDay, int meetingNumber) {
        this.userIdRecruit      = userIdRecruit;
        this.nameRecruit        = nameRecruit;
        this.titleRecruit       = titleRecruit;
        this.contentRecruit     = contentRecruit;
        this.createRecruit      = createRecruit;
        this.recruitMonth       = recruitMonth;
        this.recruitDay         = recruitDay;
        this.meetingNumber      = meetingNumber;
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

    public int getMeetingNumber() {
        return meetingNumber;
    }

    public boolean isDeadlineCheck() {
        return false;
    }


    /**
     * 채팅방에 소속된 계정 id
     * 채팅방 총 인원수
     */
    public static class Chat {
        private ArrayList<String> chatUserId;
        private int chatUserNumber;
    }
}
