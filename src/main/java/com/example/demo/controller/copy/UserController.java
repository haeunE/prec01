package com.example.demo.controller.copy;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.domain.ResponseDTO;
import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
//@RequestMapping("/auth")
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/auth/insertuser")
	public String insertUser() {
		return "user/insertUser";
	}
	
	@PostMapping("/auth/insertuser")
	@ResponseBody
	public ResponseDTO<?> insertUser(@RequestBody User user) {
		//아이디 중복 검사 
		User findUser = userService.getUser(user.getUsername());
		if(findUser.getUsername() == null) {
			userService.insertUser(user);
		
			//클라이언트에게 전달받은 user 정보를 서비스로 넘겨줌
			userService.insertUser(user);
			//정상적으로 끝나면 클라이언트한테 회원가입 완료했다고 응답
			return new ResponseDTO<>(HttpStatus.OK.value(),user.getUsername()+"님 회원가입 성공");
		}else {
			return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(),user.getUsername()+"는 중복된 아이디 입니다.");
		}
	}
	
//	@PostMapping("/auth/insertuser")
//	public String insertUser(User user) {
//		//TODO: process POST request
//		userService.insertUser(user);
//		return "";
//	}
	
	@GetMapping("/auth/login")
	public String login() {
		return "/user/login";
	}
	
	@PostMapping("/auth/login")
	@ResponseBody
	public ResponseDTO<?> login(@RequestBody User user, HttpSession session) {
		User findUser = userService.getUser(user.getUsername());
		
		if(findUser.getUsername() == null) {
			//아이디 틀림
			return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(),"없는 아이디");
		}else {
			if(findUser.getPassword().equals(user.getPassword())) {
				//로그인 성공
				session.setAttribute("principal", findUser); //브라우저가 꺼지기 전까진 정보를 저장한다. 
				return new ResponseDTO<>(HttpStatus.OK.value(), user.getUsername()+"로그인 성공");
				
			}else {
				//비번 틀림
				return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(),"비밀번호를 다시 입력하시오");
			}
		}
		
	}
	@GetMapping("/auth/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	
	
	@GetMapping("/auth/userinfo")
	public String userInfo(HttpSession session, Model model) { //model 은 넘어가는 페이지 쪽에서만 존재
		User user = (User)session.getAttribute("principal");
		System.out.println(user);
		User userInfo = userRepository.findById(user.getId()).get();
		model.addAttribute("userInfo",userInfo);
		return "user/userinfo";
	}
	
	@PostMapping("/auth/update")
	@ResponseBody
	public ResponseDTO<?> update(@RequestBody User user, HttpSession session) {
		User findUser = userService.getUser(user.getUsername());
		findUser.setPassword(user.getPassword());
		findUser.setEmail(user.getEmail());
		userRepository.save(findUser);
		session.setAttribute("principal", findUser);
		return new ResponseDTO<>(HttpStatus.OK.value(),"회원 정보 수정 완료");
	}
	
//	@PutMapping("/auth/update")
//	public String update(@RequestBody User user) {
//		User userInfo = userRepository.findById(updateData.getId()).getClass();
//		if(!updateData.getPassword().equals(""))
//			userInfo.setPassword(updateData.getPassword());
//		userInfo.setEmail(updateData.getEmail());
//		userRepository.save(userInfo);
//		return new ResponseDTO<>(HttpStatus.OK.value(),"회원정보 수정 완료");
//	}
	
	@DeleteMapping("/auth/delete")
	@ResponseBody
	public ResponseDTO<?> delete(@RequestParam int id, HttpSession session) {
		System.out.println(id);
		userRepository.deleteById(id);
		session.invalidate();
		return new ResponseDTO<>(HttpStatus.OK.value(),"탈퇴 완료");
	}
	
}
