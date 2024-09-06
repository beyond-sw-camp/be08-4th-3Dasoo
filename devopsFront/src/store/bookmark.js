import { defineStore } from 'pinia';

export const useBookmarkStore = defineStore('bookmark', {
    state: () => ({
        bookmarkList: [], // 북마크 리스트를 저장
    }),
    actions: {
        setBookmarkList(bookmarks) {
            this.bookmarkList = bookmarks;
        },
        addBookmark(newsId){
            this.bookmarkList = [...this.bookmarkList, { newsId }];
        },
        deleteBookmark(newsId){
            this.bookmarkList = this.bookmarkList.filter(bookmark => bookmark.newsId !== newsId);
        },
        clearBookmarkList() {
            this.bookmarkList = []; // 북마크 리스트 초기화
        }
    },
});
