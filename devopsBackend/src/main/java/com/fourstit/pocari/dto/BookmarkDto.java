package com.fourstit.pocari.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkDto {
    private Long bookmarkId;
    private Long newsId;
    private String newsTitle;
    private String newsSummary;
    private String categoryName;
    private String newsImage;
    private LocalDateTime createdDate;
}
