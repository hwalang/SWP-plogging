package com.example.myapplication.schema;

// 인증글 스키마
public class CertificationBoard {
    private int certifyKey;
    private String userId;
    private String certifyTitle;
    private String certifyContent;
    private String certifyImage;
    private String userName;
    private Long certifyCreate;
    private final int certifyFeel = 0;


    // 댓글 스키마
    private static class Comment {
        private int commentKey;
        private int certifyKey;
        private String userId;
        private String userName;
        private String comment;
        private Long commentCreate;
    }
}
