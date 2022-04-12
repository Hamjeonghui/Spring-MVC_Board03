package com.test.app.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.test.app.board.BoardService;
import com.test.app.board.BoardVO;

@Controller // ★x100
// 1. <bean> -> @Controller
// 2. implements Controller 필요없음 -> 제거
// 3. 메서드 강제 사라짐!!!
// 4. 완전한 POJO가 되었다!!!!!
// 		-> req,res,...가 존재하지않음 => 경량의 객체
@SessionAttributes("data") // data라는 이름의 정보가 Model에 들어온다면, session에 기억해둬라! ☆
public class BoardController {
	// 1. Controller 교체
	// 2. 반환타입의 변경 -> ModelAndView(무엇을 보낼지_정보_datas,data,member,... + 어디로 가야하는지_경로)
	
	@Autowired
	private BoardService boardService;

	@ModelAttribute("conMap") // @RequestMapping이 설정된 메서드보다 먼저 수행됨
	public Map<String,String> searchConditionMap() {
		Map<String,String> conMap=new HashMap<String,String>();
		conMap.put("제목", "title");
		conMap.put("작성자", "writer");
		conMap.put("내용", "content");
		return conMap; // 반환값은 자동으로 Model에 저장
	}
	
	@RequestMapping(value="/main.do")
	public String getBoardList(BoardVO vo,Model model) {
		// 검색 로직 추가할 예정
		// [Null체크]를 로직의 가장 처음에 수행해보려고한다!
		// "메인보여줘"요청일때는 search관련 요소들이 null임...!!
		// "검색해줘"요청일때 search관련 요소들이 존재함!
		if(vo.getSearchCondition()==null) {
			// "메인보여줘"
			vo.setSearchCondition("title");
		}
		if(vo.getSearchKeyword()==null) {
			// 검색어를 작성하지않은 경우
			vo.setSearchKeyword("");
		}
		
		model.addAttribute("datas",boardService.getBoardList(vo));
		// Model을 이용하여 전달할 정보를 저장!
		return "main.jsp";
	}
	@RequestMapping(value="/board.do")
	public String getBoard(BoardVO vo,Model model) {
		vo=boardService.getBoard(vo);
		model.addAttribute("data", vo); // ☆
		return "board.jsp";
	}
	@RequestMapping(value="/insertBoard.do")
	public String insertBoard(BoardVO vo) throws IllegalStateException, IOException {
		// 파일업로드 로직
		MultipartFile uploadFile=vo.getUploadFile();
		if(!uploadFile.isEmpty()) {
			String name=uploadFile.getOriginalFilename();
			System.out.println("파일명: "+name);
			uploadFile.transferTo(new File("D:\\KIM\\workspace\\day61\\src\\main\\webapp\\images\\"+name));
			vo.setFilename(name);
		}
		else {
			vo.setFilename("default.jpg");
		}
		boardService.insertBoard(vo);
		return "redirect:main.do";
	}
	@RequestMapping(value="/deleteBoard.do")
	public String deleteBoard(BoardVO vo) {
		boardService.deleteBoard(vo);
		return "redirect:main.do";
	}
	@RequestMapping(value="/updateBoard.do")
	public String updateBoard(@ModelAttribute("data")BoardVO vo) {  // ☆
		System.out.println(" null 업데이트 이슈 확인: "+vo.getWriter()); // session에 저장해둔 정보가 먼저 setter
		System.out.println(" null 업데이트 이슈 확인: "+vo.getTitle()); // 이후에 파라미터로 들어온 정보가 나중에 setter
		System.out.println(" null 업데이트 이슈 확인: "+vo.getContent());
		boardService.updateBoard(vo);
		return "redirect:main.do";
	}
}
