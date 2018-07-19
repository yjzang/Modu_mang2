package com.modu.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.modu.service.ModuGroupService;
import com.modu.vo.ModuGroupVo;
import com.modu.vo.ModuUserVo;

@Controller
public class GroupMainController {

	@Autowired
	private ModuGroupService groupService;

    @RequestMapping(value = "/groupmain",method = RequestMethod.GET)
    public String GroupMain(Model model, HttpSession session,@RequestParam("groupNo")int groupNo){
        System.out.println("모임메인");

        //모임 카테고리
        ModuUserVo uservo =  (ModuUserVo) session.getAttribute("authUser");
		List<ModuGroupVo> gList  = groupService.selectGroup(uservo.getUserNo());
		model.addAttribute("gList",gList);

		//클릭한 모임  메인 보여주기
		ModuGroupVo gvo = groupService.selectGroupImg(groupNo);
		model.addAttribute("gvo",gvo);

        return "/group/groupMain";
    }

    @RequestMapping("/groupSearch")
	public String Search(@RequestParam("gSearch")String gSearch,Model model,HttpSession session) {
		ModuUserVo uservo =  (ModuUserVo) session.getAttribute("authUser");

		if(uservo != null) {
			List<ModuGroupVo> gList  = groupService.selectGroup(uservo.getUserNo());
			model.addAttribute("gList",gList);
			}

		List<ModuGroupVo> searchList = groupService.searchGroup(gSearch);
		model.addAttribute("searchList",searchList);

		return"/group/groupSearch";
	}
}
