package com.example.vibecap_back.domain.member.application;

import com.example.vibecap_back.domain.member.dao.MemberRepository;
import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.member.dto.MemberDto;
import com.example.vibecap_back.domain.member.dto.SignInResult;
import com.example.vibecap_back.domain.member.dto.request.SignInRequest;
import com.example.vibecap_back.domain.member.exception.EmailAlreadyExistException;
import com.example.vibecap_back.domain.member.exception.WrongEmailException;
import com.example.vibecap_back.domain.member.exception.WrongPasswordException;
import com.example.vibecap_back.global.config.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SignService {

    private final Logger LOGGER = LoggerFactory.getLogger(SignService.class);

    private MemberRepository memberRepository;
    private JwtTokenProvider jwtTokenProvider;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public SignService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider,
            PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Google 계정을 사용하지 않고 계정 생성.
     * 이미 가입된 경우 EmailAlreadyExistException을 던진다.
     */
    public MemberDto signUp(MemberDto memberDto)
            throws EmailAlreadyExistException {
        
        String cypher;
        Member insertedMember;

        checkDuplication(memberDto.getEmail());             // 중복 확인
        cypher = passwordEncoder.encode(memberDto.getPassword());  // 암호화

        Member member = Member.builder()
                .email(memberDto.getEmail())
                .password(cypher)
                .role(memberDto.getRole().toString())
                .nickname(memberDto.getNickname())
                .status(memberDto.getStatus().toString())
                .build();

        insertedMember = memberRepository.save(member);

        return new MemberDto(insertedMember);
    }

    /**
     * Google 계정 없이 로그인
     * @param request
     * @return
     */
    public SignInResult signIn(SignInRequest request) throws WrongPasswordException, WrongEmailException {
        LOGGER.info("[SignInResult] signHandler로 회원 정보 요청");
        Member expectedMember;  // 로그인하려는 회원

        // 이메일 조회
        Optional<Member> member = memberRepository.findByEmail(request.getEmail());
        if (member.isPresent())
            expectedMember = member.get();
        else
            throw new WrongEmailException();

        // 이메일 일치 여부 확인
        if (!passwordEncoder.matches(request.getPassword(), expectedMember.getPassword()))
            throw new WrongPasswordException();

        // 인증 성공
        String token = jwtTokenProvider.createToken(expectedMember.getEmail(), expectedMember.getRole().toString());
        SignInResult result = SignInResult.builder()
                .token(token)
                .nickname(expectedMember.getNickname())
                .memberId(expectedMember.getMemberId())
                .build();

        return result;
    }

    /**
     * 이미 회원가입에 사용된 이메일이라면 예외를 발생시킨다.
     * @param email 
     * @throws EmailAlreadyExistException
     */
    private void checkDuplication(String email)
            throws EmailAlreadyExistException {
        if (memberRepository.findByEmail(email).isPresent())
            throw new EmailAlreadyExistException();
    }

}
