package shop.mtcoding.blog.board;

import lombok.Data;
import shop.mtcoding.blog.user.User;

public class BoardRequest {

    @Data
    public static class SaveDTO {
        private String title;
        private String content;

        @Data
        public static class UpdateDTO {
            private String title;
            private String content;

        }

        //이거 save하려면 Entity로 바꿔야함!
        public Board toEntity(User user) {
            return Board.builder()
                    .title(title)
                    .content(content)
                    .user(user)
                    .build();
        }

    }

    @Data
    public class UpdateDTO {
        private String title;
        private String content;
    }
}
