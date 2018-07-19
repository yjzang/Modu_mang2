package com.modu.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.modu.service.BoardService;
import com.modu.vo.BoardVo;
import com.modu.vo.FileVo;
import com.modu.vo.ModuUserVo;

@Controller
@RequestMapping("/board")
public class BoardController {

	
	@Autowired
	BoardService service;


	@RequestMapping(value="", method={RequestMethod.GET,RequestMethod.POST})
	public String goBoard(Model model){
		
		int postCheck= service.postCheck();
	    
		if(postCheck==0) {
			System.out.println("리스트 없음");
			return "/board/boardStart";
			
		} else {
		
			System.out.println("리스트 있음");
			return "/board/board";
			
		}
	}
	

	@ResponseBody
	@RequestMapping(value="/getList",method= {RequestMethod.GET, RequestMethod.POST})
	public List<BoardVo> getList(HttpSession session) {
		
		ModuUserVo authVo = (ModuUserVo)session.getAttribute("authUser");
		String userNo = String.valueOf(authVo.getUserNo());
		BoardVo boardVo = new BoardVo();
		boardVo.setUserNo(userNo);
		List<BoardVo> postList =(List<BoardVo>)service.getPostList(boardVo);
		System.out.println(postList);
		return postList;
	
	}

	
	@ResponseBody
	@RequestMapping(value="/delete",method= {RequestMethod.GET, RequestMethod.POST})
	public int deletePost(@RequestParam String boardNo) {
		
		int flag = service.deletePost(boardNo);
		return flag;
		
	}
	
	
	@RequestMapping("/write")
	public String goBoardWrite(){
		System.out.println("글쓰기 입장");
		return "/board/boardWrite";
		
	}
	
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public String addPost( @ModelAttribute BoardVo boardVo,
							@RequestParam("files") MultipartFile[] files ,
							@ModelAttribute FileVo fileVo,
							Model model) {
		
		System.out.println("글쓰기 저장 오긴 왔음");
		/*service.addPost(boardVo);*/
		
		

		System.out.println("컨트롤러 :" + fileVo.getUserNo());
		System.out.println("보드VO확인용 - " + boardVo.toString());
		System.out.println("파일VO확인용 - " + fileVo.toString());
		System.out.println("파일 확인용 - " + fileVo.toString());
	/*	System.out.println("배열로 받아지는지 보자" +file.getOriginalFilename());*/
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("fileVo", fileVo);
		map.put("files",files);
		
		service.addPost(boardVo,map);



	
		
		return "redirect:/board";
		
		
	}
	
	@ResponseBody
	@RequestMapping(value="/addCmt", method=RequestMethod.POST)
	public int addCmt(@ModelAttribute BoardVo boardVo, HttpSession session) {

		ModuUserVo authVo = (ModuUserVo)session.getAttribute("authUser");
		String userNo = String.valueOf(authVo.getUserNo());
		boardVo.setUserNo(userNo);
		System.out.println("왓썹맨~"+boardVo.toString());
		int flag = service.addCmt(boardVo);
		return flag;

	}

	@ResponseBody
	@RequestMapping(value="/getCmtList", method=RequestMethod.POST)
	public List<BoardVo> getCmtList(@ModelAttribute BoardVo boardVo) {

		List<BoardVo> list= service.getCmtList(boardVo);
		return list;
	}


	@ResponseBody
	@RequestMapping(value="/upLike")
	public BoardVo upLike(@ModelAttribute BoardVo boardVo, HttpSession session) {
		
		ModuUserVo authVo=(ModuUserVo)session.getAttribute("authUser");
		String userNo = String.valueOf(authVo.getUserNo());
		boardVo.setUserNo(userNo);
		System.out.println("스테이트"+boardVo.getLikeState());
		BoardVo resultVo = service.updateLike(boardVo);
		System.out.println("서비스 다녀온"+resultVo.getLikeState());
		System.out.println("좋아요 수"+resultVo.getLikeCount());
		
		return resultVo;
		
	}
	
	@ResponseBody
	@RequestMapping(value="/delCmt", method=RequestMethod.POST)
	public int deleteCmt(@RequestParam String commentNo){

		int flag = service.deleteCmt(commentNo);
		return flag;
	}




}
	/*
	@ResponseBody
	@RequestMapping(value="/getPostList",method=RequestMethod.POST)
	public List<BoardVo> getPostList() {
		
		
		List<BoardVo> list= service.getPostList();
		return list;
	}
	*/
	
	


