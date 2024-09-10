package com.fourstit.pocari.controller;

import com.fourstit.pocari.dto.*;
import com.fourstit.pocari.entity.Bookmark;
import com.fourstit.pocari.entity.Category;
import com.fourstit.pocari.entity.News;
import com.fourstit.pocari.entity.User;
import com.fourstit.pocari.repository.BookmarkRepository;
import com.fourstit.pocari.repository.CategoryRepository;
import com.fourstit.pocari.repository.NewsRepository;
import com.fourstit.pocari.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@Slf4j
public class MainController {
    private final UserRepository userRepository;
    private final NewsRepository newsRepository;
    private final BookmarkRepository bookmarkRepository;
    private final CategoryRepository categoryRepository;

    @GetMapping("/jenkinsTest")
    public ResponseEntity<String> jenkinsTest() {
        return ResponseEntity.ok("jenkins");
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody UserDto userDto) {
        // 사용자의 ID가 존재하는지 확인
        if (userRepository.existsById(userDto.getId())) {
            // ID로 사용자를 조회 (필요한 경우 쿼리 추가 가능)
            List<User> users = userRepository.findAll(); // 모든 사용자 가져오기
            for (User user : users) {
                if (user.getId().equals(userDto.getId()) && user.getPwd().equals(userDto.getPwd())) {
                    // 로그인 성공 시
                    return ResponseEntity.ok(user);
                }
            }
            // 비밀번호가 일치하지 않을 경우
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } else {
            // 사용자가 존재하지 않을 경우
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/main")
    public List<NewsCategoryDto> newsList() {

        return newsRepository.findAllWithCategories();
    }

    @GetMapping("/main/search")
    public List<NewsCategoryDto> searchNews(@RequestParam String query) {
        return newsRepository.searchByQuery(query);
    }

    @GetMapping("/main/{userNo}")
    public List<NewsCategoryDto> searchNews(@PathVariable("userNo") Long userNo) {
        User user = userRepository.findById(userNo).orElseThrow();

        // 타입 Long 으로 수정
        List<Long> categoryIds = Arrays.stream(user.getInterest().split(","))
                .map(Long::parseLong)
                .toList();
        // 카테고리 포함한 NewsCategoryDto로 수정
        return newsRepository.findByCategoryIdInWithCategoryName(categoryIds);
    }

    @GetMapping("/bookmark/{userNo}")
    public List<BookmarkDto> bookmarkList(@PathVariable("userNo") Long userId) {

        return bookmarkRepository.findAllByUserId(userId);
    }

    @PostMapping("/createbookmark")
    public ResponseEntity<String> createBookmark(@RequestBody BookmarkRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자: " + requestDto.getUserId()));
        News news = newsRepository.findById(requestDto.getNewsId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 뉴스: " + requestDto.getNewsId()));

        Optional<Bookmark> optionalBookmark = bookmarkRepository.findByUserIdAndNewsId(user.getUserNo(), news.getNewsId());

        if (optionalBookmark.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 추가된 뉴스입니다.");
        } else {
            Bookmark bookmark = new Bookmark();
            bookmark.setUserId(user.getUserNo());
            bookmark.setNewsId(news.getNewsId());
            bookmark.setCreatedDate(LocalDateTime.now());
            bookmarkRepository.save(bookmark);

            return ResponseEntity.ok("");
        }
    }

    @Transactional
    @DeleteMapping("/deletebookmark")
    public ResponseEntity<String> deleteBookmark(@RequestParam Long userId, @RequestParam Long newsId) {
        userRepository.findById(userId);
        newsRepository.findById(newsId);

        if (bookmarkRepository.existsByUserIdAndNewsId(userId, newsId)) {
            bookmarkRepository.deleteByUserIdAndNewsId(userId, newsId);
            return ResponseEntity.ok("");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자 북마크에 해당 뉴스가 존재하지 않음");
        }
    }

    @GetMapping("/detail/{newsNo}")
    public NewsCategoryDto getNewsById(@PathVariable("newsNo") Long newsId) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 뉴스: " + newsId));

        news.setViews(news.getViews() + 1);
        newsRepository.save(news);

        // 카테고리 포함한 NewsCategoryDto로 수정
        return newsRepository.findNewsByIdWithCategory(newsId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 뉴스: " + newsId));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getCategories() {
        List<Category> list = categoryRepository.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/user/check-userid")
    public ResponseEntity<Boolean> getChkUserId(@RequestParam String userId) {
        boolean isDuplicate = userRepository.existsById(userId);
        return ResponseEntity.ok(isDuplicate);
    }

    @PostMapping("/join")
    public ResponseEntity<String> joinUser(@RequestBody UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setPwd(userDto.getPwd());
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        user.setBirth(userDto.getBirth());
        user.setGender(userDto.getGender());
        user.setPhone(userDto.getPhone());
        user.setInsDate(LocalDate.now());
        user.setUptDate(LocalDate.now());

        StringBuilder interest = new StringBuilder();
        for (String str : userDto.getInterest()) {
            interest.append(str).append(",");
        }
        interest.deleteCharAt(interest.length() - 1);
        user.setInterest(interest.toString());

        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("");
    }

    @GetMapping("/myinfo/{userNo}")
    public ResponseEntity<UserDto> getUserInfo(@PathVariable("userNo") Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자: " + userId));

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setBirth(user.getBirth());
        userDto.setGender(user.getGender());

        // 전화번호 포맷팅
        String phone = user.getPhone();
        if (phone.length() == 11) { // "01012341234" 형식의 전화번호인 경우
            phone = phone.substring(0, 3) + "-" + phone.substring(3, 7) + "-" + phone.substring(7);
        } else if (phone.length() == 10) { // "0212341234" 형식의 전화번호인 경우
            phone = phone.substring(0, 2) + "-" + phone.substring(2, 6) + "-" + phone.substring(6);
        }

        userDto.setPhone(phone);
        userDto.setInterest(Arrays.asList(user.getInterest().split(",")));

        return ResponseEntity.ok(userDto);
    }


    @PutMapping("/myinfo/updateUserInfo/{userNo}")
    public ResponseEntity<String> updateUserInfo(@PathVariable("userNo") Long userId, @RequestBody UserDto userDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자: " + userId));

        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        user.setBirth(userDto.getBirth());
        user.setGender(userDto.getGender());
        user.setPhone(userDto.getPhone());

        userRepository.save(user);
        return ResponseEntity.ok("User information updated successfully");
    }

    @PutMapping("/myinfo/updateInterest/{userNo}")
    public ResponseEntity<String> updateUserInterest(@PathVariable("userNo") Long userNo, @RequestBody List<String> interests) {
        User user = userRepository.findById(userNo)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자: " + userNo));

        user.setInterest(String.join(",", interests));
        userRepository.save(user);

        return ResponseEntity.ok("관심사가 성공적으로 수정되었습니다.");
    }

    @PutMapping("/myinfo/updatePwd/{userNo}")
    public ResponseEntity<String> updatePassword(
            @PathVariable("userNo") Long userNo,
            @RequestParam String currentPassword,
            @RequestParam String newPassword) {

        // 사용자 조회
        User user = userRepository.findById(userNo)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자: " + userNo));

        // 현재 비밀번호 확인
        if (!user.getPwd().equals(currentPassword)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("현재 비밀번호가 일치하지 않습니다.");
        }

        // 비밀번호 업데이트
        user.setPwd(newPassword);
        userRepository.save(user);

        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
    }

    @GetMapping("/main/top")
    public List<News> getTop3News() {
        return newsRepository.findTop3ByOrderByViewsDesc();
    }

}
