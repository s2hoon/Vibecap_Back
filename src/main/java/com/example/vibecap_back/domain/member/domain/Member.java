package com.example.vibecap_back.domain.member.domain;

import com.example.vibecap_back.domain.model.Authority;
import com.example.vibecap_back.domain.model.MemberStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "googleEmail")
    private String gmail;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    // TODO @Enumerate는 성능 개선 : https://lng1982.tistory.com/279
    private Authority role;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false, name = "state")
    @Enumerated(EnumType.STRING)
    // TODO @Enumerate는 성능 개선 : https://lng1982.tistory.com/279
    private MemberStatus status;

    @Lob
    @Column
    // TODO 이 코드 그대로 진행할 경우 문제점 : https://greatkim91.tistory.com/102
    private byte[] profileImage;

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj instanceof Member)
            return this.memberId == ((Member) obj).getMemberId();
        else
            return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.memberId);
    }
}
