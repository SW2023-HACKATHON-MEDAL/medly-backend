package medal.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import medal.backend.Dto.JoinFormDto;
import medal.backend.Dto.LoginFormDto;
import medal.backend.entity.Enroll;
import medal.backend.entity.Member;
import medal.backend.repository.EnrollRepository;
import medal.backend.repository.MemberRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final EnrollRepository enrollRepository;

    /**
     * 회원가입
     */
    public Long saveMember(JoinFormDto joinFormDto) {
        Member member = Member.builder()
                .loginId(joinFormDto.getLoginId())
                .password(joinFormDto.getPassword())
                .userName(joinFormDto.getUserName())
                .phoneNumber(joinFormDto.getUserName())
                .build();
        
        Member managedMember = null;
        //관계를 맺는 경우
        if(joinFormDto.getTargetLoginId() != null) {
            managedMember = memberRepository.findByLoginId(joinFormDto.getTargetLoginId());
            if(managedMember == null) log.info("없는 회원과 관계를 맺으려 시도");
            member.setManagedMember(managedMember); // 관리할 회원과 연관관계 설정
        }

        return memberRepository.save(member).getId();
    }

    /**
     * 로그인 처리
     */
    public Member loginMember(LoginFormDto loginFormDto) {
        Member member = memberRepository.findByLoginId(loginFormDto.getLoginId());
        if(member == null) {
            log.info("없는 아이디입니다.");
        }
        if(loginFormDto.getPassword().equals(loginFormDto.getPassword())) {
            return member;
        }
        log.info("비밀번호가 틀립니다.");
        return null;
    }


    public void findManagedMemberInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(EntityNotFoundException::new);

        //관리되는 회원
        Member managedMember = memberRepository.findById(member.getManagedMember().getId())
                .orElseThrow(EntityNotFoundException::new);

        LocalTime currentTime = LocalTime.now();

        List<Enroll> morningEnrolls = new ArrayList<>();
        List<Enroll> launchEnrolls = new ArrayList<>();
        List<Enroll> dinnerEnrolls = new ArrayList<>();

        if (currentTime.isAfter(LocalTime.of(10, 0)) && currentTime.isBefore(LocalTime.of(14, 0))) {
            morningEnrolls = enrollRepository.findMorningEnrolls(memberId);
        } else if (currentTime.isAfter(LocalTime.of(14, 0)) && currentTime.isBefore(LocalTime.of(21, 0))) {
            launchEnrolls = enrollRepository.findLaunchEnrolls(memberId);
        } else if (currentTime.isAfter(LocalTime.of(21, 0))) {
            dinnerEnrolls = enrollRepository.findDinnerEnrolls(memberId);
        }
        for(Enroll enroll : morningEnrolls) {
            enroll
        }


        List<Enroll> dinnerEnrolls = enrollRepository.findDinnerEnrolls(memberId);

    }
}
