package com.example.vibecap_back.domain.member.application;

import com.example.vibecap_back.domain.member.dao.MemberRepository;
import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.member.dto.MemberDto;
import com.example.vibecap_back.domain.member.exception.EmailAlreadyExistException;
import com.example.vibecap_back.util.Encryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class MemberService {

    // private MemberRepository memberRepository;
    private MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * Google 계정을 사용하지 않고 계정 생성.
     * 이미 가입된 경우 EmailAlreadyExistException을 던진다.
     */
    public MemberDto createMemberWithoutGmail(MemberDto memberDto)
            throws EmailAlreadyExistException {
        
        String cypher;
        Member insertedMember;

        checkDuplication(memberDto.getEmail());             // 중복 확인
        cypher = encryptPassword(memberDto.getPassword());  // 암호화

        Member member = Member.builder()
                .email(memberDto.getEmail())
                .password(cypher)
                .role(memberDto.getRole())
                .nickname(memberDto.getNickname())
                .status(memberDto.getStatus())
                .build();

        insertedMember = memberRepository.save(member);

        return new MemberDto(insertedMember);
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

    /**
     * 암호화된 비밀번호를 반환한다.
     * @param pwd
     * @return
     */
    private String encryptPassword(String pwd) {
        Encryptor encryptor = new Encryptor();
        return encryptor.encrypt(pwd);
    }

}
