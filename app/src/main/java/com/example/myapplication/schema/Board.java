package com.example.myapplication.schema;

import android.net.Uri;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*게시글 스키마
* 파라미터: 유저 계정, 게시글 제목, 게시글 내용, 작성자 이름, 작성일
* */
public class Board {
    private String userId;
    private String boardTitle;
    private String boardContent;
    private String name;
    private long boardCreate;

    public Board(String userId, String boardTitle, String boardContent, String name, long boardCreate) {
        this.userId = userId;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.name = name;
        this.boardCreate = boardCreate;
    }


    /* inner class 객체 생성법
    *  Board.Certification 객체 = new 객체 Board.Certification();
    * */
    /* 인증글 스키마
    * 파라미터: 인증글key, 사진경로, 좋아요수, 좋아요 중복 방지*/
    public static class Certification {
        private int certifyKey;
        private Uri certifyPhoto;
        private final int certifyFeel = 0;
        private final Map<String, Boolean> favorites = new HashMap<>();


        public Certification(int certifyKey, Uri certifyPhoto) {
            this.certifyKey = certifyKey;
            this.certifyPhoto = certifyPhoto;
        }
    }


    /*모집글 스키마
    * 파라미터: 모집글key, 채팅key, 주소, 약속일, 클릭수, 모집인원*/
    public static class Recruitment {
        private int recruitKey;
        private int chatKey;
        private String addr;
        private Date recruitDate;
        private int recruitCount;
        private int recruitNumber;
    }
}
