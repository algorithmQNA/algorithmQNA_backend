package algorithm_QnA_community.algorithm_QnA_community.domain.alarm;

import algorithm_QnA_community.algorithm_QnA_community.domain.member.Member;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * packageName    : algorithm_QnA_community.algorithm_QnA_community.domain.alarm
 * fileName       : Alarm
 * author         : solmin
 * date           : 2023/05/01
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/05/01        solmin       최초 생성
 * 2023/05/11        solmin       DynamicInsert 추가
 * 2023/05/26        solmin       일부 필드 및 생성자, 연관관계 메소드 수정
 */
@Entity
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@DynamicInsert // RequestDto에 특정 필드가 빈 값으로 들어오는 상황에서 insert query에 null을 넣지 않고 값이 삽입되는 필드만 set
public class Alarm {
    @Id
    @GeneratedValue
    @Column(name = "alarm_id")
    private Long id;

    @Column
    private String subjectMemberName;

    private Long commentId;

    private String eventUrl;

    private boolean checked = false;

    @Column(length = 1000)
    private String msg;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlarmType type;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdDate;

    @Builder(builderClassName = "createAlarm", builderMethodName = "createAlarm")
    private Alarm(Member member, String subjectMemberName, String eventUrl, AlarmType type, String msg, Long commentId){
        this.member = member;
        this.subjectMemberName = subjectMemberName;
        this.eventUrl = eventUrl;
        this.type = type;
        this.msg = msg;
        this.member.getAlarms().add(this);
        this.commentId = commentId;
    }

    //----------------- 연관관계 필드 시작 -----------------//

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    //----------------- 연관관계 메소드 시작 -----------------//

    public void check(){
        this.checked = true;
    }

    public void deleteAlarm(){
        this.member = null;
    }
}
