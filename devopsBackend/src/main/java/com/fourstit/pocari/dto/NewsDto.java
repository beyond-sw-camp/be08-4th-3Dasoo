package com.fourstit.pocari.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fourstit.pocari.entity.News;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsDto {
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdDate;

    private String author;

    private String content;

    private String img;

    private String newsPaper;

    private String summary;

    private String title;

    private long categoryId;

    private int views;

    public static NewsDto fromEntity(News news) {
        NewsDto newsDto = new NewsDto();
        newsDto.setId(news.getNewsId());
        newsDto.setCreatedDate(news.getCreatedDate());
        newsDto.setAuthor(news.getAuthor());
        newsDto.setContent(news.getContent());
        newsDto.setImg(news.getImage());
        newsDto.setNewsPaper(news.getNewspaper());
        newsDto.setSummary(news.getSummary());
        newsDto.setTitle(news.getTitle());
        newsDto.setCategoryId(news.getCategoryId());
        newsDto.setViews(news.getViews());
        return newsDto;
    }

    public News toEntity() {
        News news = new News();
        news.setNewsId(this.getId());
        news.setCreatedDate(this.getCreatedDate());
        news.setAuthor(this.getAuthor());
        news.setContent(this.getContent());
        news.setImage(this.getImg());
        news.setNewspaper(this.getNewsPaper());
        news.setSummary(this.getSummary());
        news.setTitle(this.getTitle());
        news.setCategoryId(news.getCategoryId());
        news.setViews(this.getViews());
        return news;
    }

}
