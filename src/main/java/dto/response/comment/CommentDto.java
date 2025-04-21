package dto.response.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentDto {

    private String id;
    private String articleId;
    private String userId;
    private String userNickname;
    private String content;
    private int likeCount;
    private boolean likedByMe;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
}
