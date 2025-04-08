package shop.mtcoding.blog.board;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

public class BoardResponse {

    // 상세보기 화면에 필요한 데이터
    @AllArgsConstructor
    @Data
    public static class DetailDTO {
        private Integer id;
        private String title;
        private String content;
        private Boolean isPublic;
        private Boolean isOwner;

        private String username;
        private Timestamp createdAt;
        private Long loveCount; // 그룹함수로 리턴되는 숫자는 Long 타입
        private Boolean isLove;


    }

}
