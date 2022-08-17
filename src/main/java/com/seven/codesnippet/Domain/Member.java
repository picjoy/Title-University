package com.seven.codesnippet.Domain;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;
/*
        카카오톡
    @Column(nullable = false)
    private Long kakaoId;
*/

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        //프록시(Proxy)는 대리자 라는 뜻으로, 클라이언트가 사용하려고 하는 실제 대상인 것처럼 위장해서 클라이언트의 요청을 받아주는 역할
        //JPA와 Hibernate는 마치 자바의 interface와 해당 interface를 구현한 class와 같은 관계
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Member member = (Member) o;
        return id != null && Objects.equals(id, member.id);
    }
    // equals 로 비교를 하는데 이 때 실제 메모리 코드로 비교를 해야되기때문에 hashCode를 쓴다.
    // 작성자 확인을 확실하게 하기 위해 사용한다!
    // 인증값을 아이디값으로 하기위해 이 로직을 사용하는 것이다.( 댓글이나 게시글 crud)
    // solid => openclose 이 객체를 확인하는 용으로  => 이 객체가 맞는 지 확인하기 위해서 사용하기 => 인증 처리 사용한다
    // 실제 메모리의 주소값을 알 수 있다.

    @Override // 패스워드 hashCode는 일반적으로 각 객체의 주소값을 변환하여 생성한 객체의 고유한 정수값이다. 따라서 두 객체가 동일 객체인지 비교할 때 사용
    public int hashCode() {
        return getClass().hashCode();
    }

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<com.seven.codesnippet.Domain.TitlePost> postList;

    public boolean validatePassword(PasswordEncoder passwordEncoder, String password) {
        return passwordEncoder.matches(password, this.password);
    }
/*
        카카오톡
    public boolean Member (Object o) {
        if (this== o){
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)){
            return false;
        }
        Member member = (Member) o;
        return id != null && Objects.equals(id, member.id);
    }

*/


}
