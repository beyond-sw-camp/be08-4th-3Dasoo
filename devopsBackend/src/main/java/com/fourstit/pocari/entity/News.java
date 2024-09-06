package com.fourstit.pocari.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tb_news")
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_id")
    private Long newsId; // PK

    @Column(name = "title", nullable = false)
    private String title; // 제목

    @Column(name = "content", columnDefinition = "text", nullable = false)
    private String content; // 상세내용

    @Column(name = "category_id")
    private long categoryId; // 카테고리번호

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate; // 작성일

    @Column(name = "author", nullable = false)
    private String author; // 작성자

    @Column(name = "newspaper", nullable = false)
    private String newspaper; // 신문사

    @Column(name = "summary", nullable = false)
    private String summary; //요약내용

    @Column(name = "image", nullable = false)
    private String image;

    @Column(name = "views")
    private int views;
}