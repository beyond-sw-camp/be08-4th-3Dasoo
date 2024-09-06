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
@Table(name = "tb_bookmark")
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id")
    private Long bookmarkId; // PK

    @Column(name = "user_no", nullable = false)
    private Long userId; // 회원번호

    @Column(name = "news_id", nullable = false)
    private Long newsId; // 뉴스번호

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate; // 등록일
}