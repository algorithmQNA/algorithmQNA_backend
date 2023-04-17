package algorithm_QnA_community.algorithm_QnA_community.domain.dto;

import lombok.Data;

@Data
public class ResponseTokenAndMember {
    private String accessToken;
    private String memberId;
    private String memberName;

    public ResponseTokenAndMember(String accessToken, String memberId, String memberName) {
        this.accessToken = accessToken;
        this.memberId = memberId;
        this.memberName = memberName;
    }
}
