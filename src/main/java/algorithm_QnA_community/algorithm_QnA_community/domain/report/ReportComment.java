package algorithm_QnA_community.algorithm_QnA_community.domain.report;

import algorithm_QnA_community.algorithm_QnA_community.domain.comment.Comment;
import algorithm_QnA_community.algorithm_QnA_community.domain.member.Member;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * packageName    : algorithm_QnA_community.algorithm_QnA_community.domain
 * fileName       : LikeComment
 * author         : solmin
 * date           : 2023/04/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/04/26        solmin       최초 생성
 * 2023/05/01        solmin       DynamicInsert및 update 추가, 일부 Validation 변경,
 *                                update method 통합
 * 2023/05/10        solmin       팩토리 메소드 일부 수정
 * 2023/05/11        solmin       Docs 변경, detail null = false 로 변경
 * 2023/05/23        solmin       삭제 편의 메소드 추가 및 일부 접근제한자 변경
 */
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@DynamicInsert // RequestDto에 특정 필드가 빈 값으로 들어오는 상황에서 insert query에 null을 넣지 않고 값이 삽입되는 필드만 set
@DynamicUpdate // RequestDto에 특정 필드가 빈 빈 값으로 들어오는 상황에서 update query에 null을 넣지 않고 변경된 필드만 set
@EntityListeners(AuditingEntityListener.class)
public class ReportComment {
    @Id
    @GeneratedValue
    @Column(name = "report_comment_id")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReportCategory reportCategory;

    @ColumnDefault("'기타 사유 없음'")
    @Column(length = 1000, nullable = false)
    private String detail;

    @LastModifiedDate
    @Column(name="updated_at")
    private LocalDateTime lastModifiedDate;

    @Builder(builderClassName = "createReportComment", builderMethodName = "createReportComment")
    public ReportComment(Comment comment, Member member,
                         ReportCategory reportCategory, String detail){
        this.member = member;
        this.comment = comment;
        this.comment.getReportComments().add(this);
        this.reportCategory = reportCategory;
        this.detail = detail;
    }

    public void updateReportInfo(@NonNull ReportCategory category, String detail){
        this.detail = detail;
        this.reportCategory = category;
    }

    //----------------- 연관관계 필드 시작 -----------------//

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    public void deleteReportComment() {
        this.member = null;
        this.comment = null;
    }
}
