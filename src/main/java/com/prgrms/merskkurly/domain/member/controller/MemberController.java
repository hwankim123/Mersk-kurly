package com.prgrms.merskkurly.domain.member.controller;

import static com.prgrms.merskkurly.domain.common.auth.SessionAttributes.ID;

import com.prgrms.merskkurly.domain.common.exception.UnAuthorizedException;
import com.prgrms.merskkurly.domain.member.dto.MemberRequest;
import com.prgrms.merskkurly.domain.member.dto.MemberResponse;
import com.prgrms.merskkurly.domain.member.entity.Role;
import com.prgrms.merskkurly.domain.member.service.MemberService;
import java.net.URI;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
public class MemberController {

  private final MemberService memberService;

  public MemberController(MemberService memberService){
    this.memberService = memberService;
  }

  @GetMapping("/signup")
  public ResponseEntity<MemberResponse.SignupData> signup(){
    List<Role> values = List.of(Role.values());
    MemberResponse.SignupData signupData = new MemberResponse.SignupData(values);
    return ResponseEntity.ok(signupData);
  }

  @PostMapping("/signup")
  public ResponseEntity<?> signup(@RequestBody MemberRequest.SignupForm signupForm){
    memberService.signup(signupForm);

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(URI.create("/"));
    return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
  }

  @GetMapping("/login")
  public ResponseEntity<MemberResponse.LoginInfo> login(@RequestBody MemberRequest.LoginForm loginForm, HttpServletRequest request){
    MemberResponse.LoginInfo loginInfo = memberService.login(loginForm);

    HttpSession session = request.getSession();
    session.setAttribute("id", loginInfo.getId());
    session.setAttribute("name", loginInfo.getName());
    session.setAttribute("role", loginInfo.getRole().toString());

    return ResponseEntity.ok(loginInfo);
  }

  @GetMapping("/mypage")
  public ResponseEntity<MemberResponse.Details> mypage(HttpServletRequest request){
    HttpSession session = request.getSession();
    Long id = (Long) session.getAttribute(ID);
    if(id == null){
      throw new UnAuthorizedException("UnAuthorized");
    }

    MemberResponse.Details details = memberService.findById(id);
    return ResponseEntity.ok(details);
  }

  @PutMapping("/mypage")
  public ResponseEntity<?> update(@RequestBody MemberRequest.UpdateForm updateForm, HttpServletRequest request){
    HttpSession session = request.getSession();
    Long id = (Long) session.getAttribute(ID);
    if(id == null){
      throw new UnAuthorizedException("UnAuthorized");
    }
    memberService.update(id, updateForm);

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(URI.create("/member/mypage"));
    return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
  }

  @GetMapping("/logout")
  public ResponseEntity<?> logout(HttpServletRequest request){
    HttpSession session = request.getSession();
    session.invalidate();

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(URI.create("/"));
    return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
  }
}
