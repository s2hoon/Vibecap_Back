package com.example.vibecap_back.domain.member.domain;

import com.example.vibecap_back.domain.model.MemberStatus;
import com.example.vibecap_back.domain.post.domain.Like.Likes;
import com.example.vibecap_back.domain.post.domain.Scrap.Scrap;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="member")
public class Member implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @Column(name = "member_id")
    @JsonProperty("member_id")
    private Long memberId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "google_email")
    private String gmail;

    @Column(nullable = false)
    // TODO @Enumerate는 성능 개선 : https://lng1982.tistory.com/279
    private String role;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false, name = "state")
    // TODO @Enumerate는 성능 개선 : https://lng1982.tistory.com/279
    private String status;

    @Column(name = "profile_image")
    @JsonProperty("profile_image")
    // TODO 이 코드 그대로 진행할 경우 문제점 : https://greatkim91.tistory.com/102
    private String profileImage;

    /** 좋아요 count **/
    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Likes> postLikeList = new ArrayList<>();

    public void mappingPostLike(Likes postLike) {
        this.postLikeList.add(postLike);
    }

    /** 스크랩 count **/
    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Scrap> postScrapList = new ArrayList<>();

    public void mappingPostScrap(Scrap postScrap) {
        this.postScrapList.add(postScrap);
    }

    /************ UserDetails interface 구현 ************/
    // TODO Collection framework 사용법, UserDetails interface 공부...
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<String> roles = new ArrayList<>();
        roles.add(this.role);

        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    /**
     * 일반적으로 계정의 아이디를 반환.
     * @return
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * 만료된 계정인지 확인 (해당 기능 구현하지 않음)
     * @return
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // response에는 포함하지 않는다.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 잠긴 계정인지 확인 (해당 기능 구현하지 않음)
     * @return
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 비밀번호가 만료되었는지 확인
     * @return
     * true : 만료되지 않음
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 계정이 활성화 상태인지 확인.
     * @return
     * true : 활성화
     * false : 휴면 또는 탈퇴한 계정
     */
    @Override
    public boolean isEnabled() {
        return (status == MemberStatus.ACTIVE.toString());
    }


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
