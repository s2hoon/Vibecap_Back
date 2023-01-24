package com.example.vibecap_back.domain.member.application;

import com.example.vibecap_back.domain.member.dao.MemberRepository;
import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.member.dto.ChangeNicknameResult;
import com.example.vibecap_back.domain.member.dto.QuitResult;
import com.example.vibecap_back.domain.member.dto.request.ChangeNicknameRequest;
import com.example.vibecap_back.domain.member.dto.request.QuitRequest;
import com.example.vibecap_back.domain.model.MemberStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class MemberInfoService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberInfoService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public ChangeNicknameResult updateMemberNickname(ChangeNicknameRequest request) {

        Optional<Member> optionalMember = memberRepository.findById(request.getMemberId());
        // 이미 로그인에 성공한 회원이라면 반드시 찾을 수 있다.
        Member member = optionalMember.get();
        member.setNickname(request.getNewNickname());
        Member changedMember = memberRepository.save(member);

        ChangeNicknameResult result = new ChangeNicknameResult(changedMember.getNickname());
        return result;
    }

    public QuitResult updateMemberStatus(QuitRequest request) {

        Optional<Member> optionalMember = memberRepository.findById(request.getMemberId());
        // 이미 로그인에 성공한 회원이라면 반드시 찾을 수 있다.
        Member member = optionalMember.get();
        member.setStatus(MemberStatus.QUIT.toString());
        Member quitMember = memberRepository.save(member);

        QuitResult result = new QuitResult(quitMember.getNickname());
        return result;
    }

    public Long updateProfileImage(Long memberId, byte[] image) {

        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Member member = optionalMember.get();
        member.setProfileImage(image);

        return memberRepository.save(member).getMemberId();
    }
}
