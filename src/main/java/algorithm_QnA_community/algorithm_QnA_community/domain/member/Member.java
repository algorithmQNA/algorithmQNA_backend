package algorithm_QnA_community.algorithm_QnA_community.domain.member;

import algorithm_QnA_community.algorithm_QnA_community.domain.BaseTimeEntity;
import algorithm_QnA_community.algorithm_QnA_community.domain.alarm.Alarm;
import algorithm_QnA_community.algorithm_QnA_community.domain.post.Post;
import algorithm_QnA_community.algorithm_QnA_community.domain.comment.Comment;
import algorithm_QnA_community.algorithm_QnA_community.utils.listner.MemberBadgeUpdateListener;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * packageName    : algorithm_QnA_community.algorithm_QnA_community.domain
 * fileName       : Member
 * author         : solmin
 * date           : 2023/04/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/04/26        solmin       최초 생성, Member 엔티티와 다대다 관계를 갖는 각 엔티티에 대해
 *                                멤버가 이러한 매핑정보들을 필수적으로 알아야 하는지 궁금
 * 2023/05/01        solmin       Validation 일부 추가 및 Alarms Mapping
 * 2023/05/11        solmin       DynamicInsert, DynamicUpdate 추가
 * 2023/05/16        solmin       엔티티 삭제를 위한 orphanRemoval 추가
 * 2023/05/26        solmin       멤버 뱃지상태 감지를 위한 @Transient 필드 추가 및 연관관계 메소드 추가
 */

@Entity
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@DynamicInsert // RequestDto에 특정 필드가 빈 값으로 들어오는 상황에서 insert query에 null을 넣지 않고 값이 삽입되는 필드만 set
@DynamicUpdate // RequestDto에 특정 필드가 빈 빈 값으로 들어오는 상황에서 update query에 null을 넣지 않고 변경된 필드만 set
@EntityListeners(value = {AuditingEntityListener.class, MemberBadgeUpdateListener.class})
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Email
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "멤버 이름을 공백을 제외하고 2글자 이상 20글자 이하로 작성하세요.")
    @Size(min=2, max=20, message = "멤버 이름을 공백을 제외하고 2글자 이상 20글자 이하로 작성하세요.")
    private String name;

    @Column(nullable = false)
    private Role role;

    private int commentBadgeCnt;
    private int postBadgeCnt;
    private int likeBadgeCnt;

    @Transient
    private int preCommentBadgeCnt;
    @Transient
    private int prePostBadgeCnt;
    @Transient
    private int preLikeBadgeCnt;

    @Column(length = 1000)
    private String profileImgUrl;

    @Builder(builderClassName = "createMember", builderMethodName = "createMember")
    public Member(String name, String email, Role role, String profileImgUrl){
        this.name = name;
        this.email = email;
        this.role = role;
        this.profileImgUrl = profileImgUrl;
    }
    //----------------- 연관관계 필드 시작 -----------------//

    @OneToMany(mappedBy = "member", orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member", orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "member", orphanRemoval = true)
    private List<Alarm> alarms = new ArrayList<>();

    // 멤버가 이 정보들을 알고 있지 않는게 낫다고 판단
//    @OneToMany(mappedBy = "member")
//    private List<LikeComment> likeComments = new ArrayList<>();
//
//    @OneToMany(mappedBy = "member")
//    private List<LikePost> likePosts = new ArrayList<>();
//
//    @OneToMany(mappedBy = "member")
//    private List<ReportComment> reportComments = new ArrayList<>();
//
//    @OneToMany(mappedBy = "member")
//    private List<ReportPost> reportPosts = new ArrayList<>();

    //----------------- 연관관계 메소드 시작 -----------------//

    public void updateMemberBadgeCnt(Badge state, int value){
        switch(state){
            case POST:
                this.postBadgeCnt+=value;
                break;
            case COMMENT:
                this.commentBadgeCnt+=value;
                break;
            case Like:
                this.likeBadgeCnt+=value;
                break;
        }
    }
    public void savePreBadgeCnt(){
        this.preCommentBadgeCnt = commentBadgeCnt;
        this.prePostBadgeCnt = postBadgeCnt;
        this.preLikeBadgeCnt = likeBadgeCnt;
    }

    public void updateProfile(String profileImgUrl){
        this.profileImgUrl = profileImgUrl;
    }

    public void updateName(String memberName){
        this.name = memberName;
    }

}
