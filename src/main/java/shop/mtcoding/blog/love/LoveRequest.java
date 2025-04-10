package shop.mtcoding.blog.love;

import lombok.Data;
import shop.mtcoding.blog.board.Board;
import shop.mtcoding.blog.user.User;

public class LoveRequest {

    @Data
    public static class SaveDTO {
        private Integer boardId;

        public Love toEntity(Integer sessionUser) {
            return Love.builder()
                    .board(Board.builder().id(boardId).build())
                    .user(User.builder().id(sessionUser).build())
                    .build();
        }
    }
}
