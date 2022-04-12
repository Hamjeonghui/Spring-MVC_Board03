package com.test.app.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.test.app.member.MemberService;
import com.test.app.member.MemberVO;

@Controller
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	
	@RequestMapping(value="/login.do",method=RequestMethod.GET)
	public String login(@ModelAttribute("user")MemberVO vo) {
		System.out.println(" GET로 login.do요청");
		vo.setId("test");
		vo.setPassword("1234");
		return "login.jsp";
	}
	@RequestMapping(value="/login.do",method=RequestMethod.POST)
	public String login(MemberVO vo,HttpSession session) {
		System.out.println(" POST로 login.do요청");
		if(vo.getId()==null||vo.getId().equals("")) {
			throw new IllegalArgumentException("아이디 입력없이 login.do를 요청하였습니다.");
		}
		
		vo=memberService.getMember(vo);
		if(vo==null){
			return "redirect:login.jsp";
		}
		session.setAttribute("member", vo);
		return "redirect:main.do";
	}
	@RequestMapping("/logout.do")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:login.jsp";
	}
	@RequestMapping("/insertMember.do")
	public String insertMember(MemberVO vo) {
		memberService.insertMember(vo);
		return "redirect:login.jsp";
	}
	@RequestMapping("/deleteMember.do")
	public String deleteMember(MemberVO vo) {
		memberService.deleteMember(vo);
		return "redirect:login.jsp";
	}
	@RequestMapping("/updateMember.do")
	public String updateMember(MemberVO vo) {
		memberService.updateMember(vo);
		return "redirect:main.do";
	}
}
