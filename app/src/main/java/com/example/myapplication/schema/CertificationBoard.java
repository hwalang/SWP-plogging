package com.example.myapplication.schema;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*인증글 스키마
* 파라미터:
* 1. 유저 계정, 게시글 제목, 게시글 내용, 작성자 이름, 작성일
* 2. 사진경로, 좋아요수, 좋아요 중복 방지
* */
public class CertificationBoard {
    // 인증글 key 는 파이어스토어에서 자동 생성 add() 이용
    private String userId;
    private String boardTitle;
    private String boardContent;
    private String name;
    private long boardCreate;

    private String certifyPhoto;
    private final long certifyFeel = 0;
    private final Map<String, Boolean> favorites = new HashMap<>();

    public CertificationBoard(String userId, String boardTitle, String boardContent, String name, long boardCreate, String certifyPhoto) {
        this.userId = userId;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.name = name;
        this.boardCreate = boardCreate;
        this.certifyPhoto = certifyPhoto;
    }

    public String getUserId() {
        return userId;
    }

    public String getBoardTitle() {
        return boardTitle;
    }

    public String getBoardContent() {
        return boardContent;
    }

    public String getName() {
        return name;
    }

    public long getBoardCreate() {
        return boardCreate;
    }

    public String getCertifyPhoto() {
        return certifyPhoto;
    }

    public long getCertifyFeel() {
        return certifyFeel;
    }

    public Map<String, Boolean> getFavorites() {
        return favorites;
    }
//    /*모집글 스키마
//    * 파라미터:
//    3. 모집글key, 채팅key, 주소, 약속일, 클릭수, 모집인원*/
//    public static class Recruitment {
//    private long recruitKey;
//    private long chatKey;
//    private String address;
//    private Date recruitDate;
//    private long recruitCount;
//    private long recruitNumber;
//    }
}
