package algorithm_QnA_community.algorithm_QnA_community.domain.post;

import algorithm_QnA_community.algorithm_QnA_community.domain.BaseTimeEntity;
import algorithm_QnA_community.algorithm_QnA_community.domain.comment.Comment;
import algorithm_QnA_community.algorithm_QnA_community.domain.like.LikePost;
import algorithm_QnA_community.algorithm_QnA_community.domain.member.Member;
import algorithm_QnA_community.algorithm_QnA_community.domain.member.Role;
import algorithm_QnA_community.algorithm_QnA_community.domain.report.ReportPost;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * packageName    : algorithm_QnA_community.algorithm_QnA_community.domain
 * fileName       : Post
 * author         : solmin
 * date           : 2023/04/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/04/26        solmin       최초 생성
 * 2023/05/01        solmin       불필요한 setter 삭제 및 일부 Validation 추가
 *                                TEXT->LONGTEXT
 * 2023/05/02        solmin       조회수 기능 추가 -> 추후 쿠키를 이용해서 중복 피할 예정
 *                                LONGTEXT -> TEXT로 변경 (요구사항이 default page size = 16K를 초과하지 않음)
 *                                추가로 XSS 방지를 위해 스크립트를 HTML 엔티티로 인코딩 이후 조회 시 디코딩하는 작업 필요
 * 2023/05/11        solmin       DynamicInsert, DynamicUpdate 추가
 * 2023/05/11        janguni      PostType 변수 추가
 * 2023/05/12        janguni      updateTitle, updateContent, updateCategory, updateType 추가
 * 2023/05/16        solmin       엔티티 삭제를 위한 orphanRemoval 추가
 * 2023/05/16        janguni      updateViews 추가
 * 2023/05/18        janguni      Member연관관계 CascadeType.ALL -> CascadeType.PERSIST로 변경


 */
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@DynamicInsert // RequestDto에 특정 필드가 빈 값으로 들어오는 상황에서 insert query에 null을 넣지 않고 값이 삽입되는 필드만 set
@DynamicUpdate // RequestDto에 특정 필드가 빈 빈 값으로 들어오는 상황에서 update query에 null을 넣지 않고 변경된 필드만 set
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    @NotBlank
    private String content;

    private int likeCnt;
    private int dislikeCnt;

    private int views;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostType type;

    @Builder(builderClassName = "createPost", builderMethodName = "createPost")
    public Post(Member member, String title, String content, PostCategory category, PostType type){
        this.member = member;
        member.getPosts().add(this);
        this.title = title;
        this.content = content;
        this.category = category;
        this.type = type;
    }

    //----------------- 연관관계 필드 시작 -----------------//

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<LikePost> likePosts = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<ReportPost> reportPosts = new ArrayList<>();



    //----------------- 연관관계 메소드 시작 -----------------//

    public void updateLikeCnt(boolean isLike, boolean isIncrement){
        if(isLike){
            likeCnt = isIncrement? likeCnt+1 : likeCnt-1;
        }else{
            dislikeCnt = isIncrement? dislikeCnt+1 : dislikeCnt-1;
        }
    }

    public void updateTitle(String changedTitle){
        this.title = changedTitle;
    }

    public void updateContent(String changedContent){
        this.content = changedContent;
    }

    public void updateCategory(PostCategory changedCategory) {
        this.category = changedCategory;
    }

    public void updateType(PostType changedType) {
        this.type = changedType;
    }

    public void updateViews(){
        this.views +=1;
    }


}
