package com.fourstit.pocari.dto;

import com.fourstit.pocari.entity.News;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsCategoryDto {

    private Long newsId; // PK

    private String title; // 제목

    private String content; // 상세내용

    private long categoryId; // 카테고리번호

    private LocalDateTime createdDate; // 작성일

    private String author; // 작성자

    private String newspaper; // 신문사

    private String summary; //요약내용

    private String image;

    private int views;

    private String categoryName;


}
