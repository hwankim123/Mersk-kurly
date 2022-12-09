package com.prgrms.merskkurly.domain.member.service;

import com.prgrms.merskkurly.domain.common.exception.NotFoundException;
import com.prgrms.merskkurly.domain.member.dto.MemberRequest;
import com.prgrms.merskkurly.domain.member.dto.MemberResponse;
import com.prgrms.merskkurly.domain.member.entity.Member;
import com.prgrms.merskkurly.domain.member.entity.Role;
import com.prgrms.merskkurly.domain.member.repository.MemberRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberService {

  private final MemberRepository memberRepository;

  public MemberService(MemberRepository memberRepository){
    this.memberRepository = memberRepository;
  }

  @Transactional(readOnly = true)
  public MemberResponse.LoginInfo login(MemberRequest.LoginForm loginForm) {
    Optional<Member> nullableMember = memberRepository.findByUsername(loginForm.getUsername());
    Member member = nullableMember.orElseThrow(
        () -> new IllegalArgumentException("wrong username or password"));

    member.verifyPassword(loginForm.getPassword());

    return new MemberResponse.LoginInfo(member.getId(), member.getName(), member.getUsername(), member.getRole());
  }

  @Transactional(readOnly = true)
  public MemberResponse.Details findById(Long id){
    Optional<Member> nullableMember = memberRepository.findById(id);
    Member member = nullableMember.orElseThrow(
        () -> new NotFoundException("Not found member id: " + id));

    return MemberResponse.Details.from(member);
  }

  public void signup(MemberRequest.SignupForm signupForm){
    Member member = Member.newInstance(
        signupForm.getName(),
        signupForm.getUsername(),
        signupForm.getPassword(),
        Role.valueOf(signupForm.getRole()),
        signupForm.getAddress());

    memberRepository.save(member);
  }

  public void update(Long id, MemberRequest.UpdateForm updateForm){
    Optional<Member> nullableMember = memberRepository.findById(id);
    Member member = nullableMember.orElseThrow(
        () -> new NotFoundException("Not found member id: " + id));

    member.update(updateForm.getPassword(), Role.valueOf(updateForm.getRole()), updateForm.getAddress());

    memberRepository.update(member);
  }

  public void delete(Long id){
    memberRepository.delete(id);
  }
}
