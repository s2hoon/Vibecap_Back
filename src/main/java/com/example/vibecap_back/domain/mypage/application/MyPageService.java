package com.example.vibecap_back.domain.mypage.application;

import com.example.vibecap_back.domain.member.domain.Member;
import com.example.vibecap_back.domain.mypage.dao.MyPageRepository;
import com.example.vibecap_back.domain.mypage.dao.MyPostsRepository;
import com.example.vibecap_back.domain.mypage.dto.response.GetMyPageResponse;
import com.example.vibecap_back.domain.mypage.exception.InvalidMemberException;
import com.example.vibecap_back.global.common.response.BaseException;
import com.example.vibecap_back.global.config.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Service
public class MyPageService {

    private final Logger LOGGER = LoggerFactory.getLogger(MyPageService.class);

    private final MyPageRepository myPageRepository;
    private final MyPostsRepository myPostsRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public MyPageService(MyPageRepository myPageRepository, MyPostsRepository myPostsRepository, JwtTokenProvider jwtTokenProvider) {
        this.myPageRepository = myPageRepository;
        this.myPostsRepository = myPostsRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    // 마이페이지 조회 (사용자 정보 load)
    public GetMyPageResponse getMyPage(GetMyPageResponse request) throws BaseException, IOException {
        Optional<Member> optionalMember = myPageRepository.findById(request.getMemberId());
        Member member = optionalMember.get();

        byteArrayConvertToImageFile(member.getProfileImage());  // 이미지 저장 확인용

        return new GetMyPageResponse(member.getMemberId(), member.getEmail(),
                member.getGmail(), member.getNickname(), member.getProfileImage());
    }

    // 프로필 이미지 변경
    public void updateProfileImage(Long memberId, MultipartFile profileImage) throws IOException {
        Optional<Member> optionalMember = myPageRepository.findById(memberId);
        Member member = optionalMember.get();
        member.setProfileImage(profileImage.getBytes());
        myPageRepository.save(member);
    }

    // 바이트 배열을 로컬에 이미지 파일로 저장
    public void byteArrayConvertToImageFile(byte[] imageByte) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(imageByte);
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        ImageIO.write(bufferedImage, "png", new File("C:\\Users\\최지은\\Downloads\\image.png")); //저장하고자 하는 파일 경로를 입력
    }

    // 토큰으로 회원 권한 검사
    public void checkMemberValid(Long memberId) throws InvalidMemberException {
        // JWT 에서 email 추출
        String email = jwtTokenProvider.extractEmail();
        Optional<Member> member = myPageRepository.findByEmail(email);

        // memberId와 접근한 회원이 같은지 확인
        if (!Objects.equals(memberId, member.get().getMemberId())) {
            throw new InvalidMemberException();
        }
    }

}
