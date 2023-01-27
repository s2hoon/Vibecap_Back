package com.example.vibecap_back.domain.member.application;

import com.example.vibecap_back.domain.member.dao.MemberRepository;
import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.member.dto.ChangeNicknameResult;
import com.example.vibecap_back.domain.member.dto.QuitResult;
import com.example.vibecap_back.domain.member.dto.request.ChangeNicknameRequest;
import com.example.vibecap_back.domain.member.dto.request.QuitRequest;
import com.example.vibecap_back.domain.model.MemberStatus;
import com.example.vibecap_back.global.config.storage.FileSaveErrorException;
import com.example.vibecap_back.global.config.storage.FireBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class MemberInfoService {

    private final FireBaseService fireBaseService;
    private final MemberRepository memberRepository;
    private static final String DISABLED_SUFFIX = " DISABLED";

    @Autowired
    public MemberInfoService(FireBaseService fireBaseService, MemberRepository memberRepository) {
        this.fireBaseService = fireBaseService;
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

    public QuitResult disableMember(QuitRequest request) {

        Optional<Member> optionalMember = memberRepository.findById(request.getMemberId());
        String originEmail;
        // 이미 로그인에 성공한 회원이라면 반드시 찾을 수 있다.
        Member member = optionalMember.get();
        originEmail = member.getEmail();
        member.setStatus(MemberStatus.QUIT.toString());
        // 탈퇴한 회원의 이메일이 검색되지 않도록 가린다ㅂ.
        member.setEmail(originEmail + DISABLED_SUFFIX);
        Member quitMember = memberRepository.save(member);

        QuitResult result = new QuitResult(quitMember.getNickname());
        return result;
    }

    public Long updateProfileImage(Long memberId, MultipartFile image) throws IOException, FileSaveErrorException {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Member member = optionalMember.get();

        // 프로필 이미지 존재할 경우, firebase 에서 삭제
        if(member.getProfileImage() != null && member.getProfileImage().length() != 0) {
            String fileName = fireBaseService.getFileName(member.getProfileImage());
            fireBaseService.delete(fileName);
        }
        String profileImgUrl = fireBaseService.uploadFiles(image);
        member.setProfileImage(profileImgUrl);

        return memberRepository.save(member).getMemberId();
    }
}
