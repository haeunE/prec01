package com.example.demo.controller.copy;

import java.net.http.HttpRequest;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.domain.PageDTO;
import com.example.demo.domain.Post;
import com.example.demo.domain.ResponseDTO;
import com.example.demo.domain.User;
import com.example.demo.repository.PostRepository;
import com.example.demo.service.PostService;
import org.springframework.web.bind.annotation.RequestParam;





@Controller
public class PostController {
	
	@Autowired
	private PostService postservice;
	
	@Autowired
	private PostRepository postrepository;
	
	@GetMapping("/post")
	public String insertPost() {
		return "post/insertPost";
	}
	
	@PostMapping("/post")
	@ResponseBody
	public ResponseDTO<?> insertPost(@RequestBody Post post, HttpSession session){
		User getUser = (User)session.getAttribute("principal");
		postservice.insertPost(post, getUser);
		return new ResponseDTO<>(HttpStatus.OK.value(),"게시물 등록 완료");
	}
	
	@GetMapping({"","/"})
	public String getPostList(Model model,@PageableDefault(size = 10, sort="id", direction = Direction.DESC) Pageable pageable) {
		Page<Post> postList = postservice.getPostList(pageable);//page는 number변수
		model.addAttribute("postList",postList);
		
		//PageDTO 이용하기
		model.addAttribute("pageDTO", new PageDTO(postList));
		
		return "index";
	}
	@PostMapping("")
	public String postMethodName(@RequestBody String entity) {
		//TODO: process POST request
		
		return entity;
	}
	
	@GetMapping("/post/{id}")
	public String getPost(@PathVariable int id, Model model) {
		Post getPost = postservice.getPost(id);
		model.addAttribute("post",getPost);
		
		return "post/detail";
	}
	
	@GetMapping("/post/modify/{id}")
	public String modify(@PathVariable int id, Model model) {
		Post getPost = postservice.getPost(id);
		model.addAttribute("post", getPost);
		return "post/modify";
	}
	
	@PutMapping("/post")
	@ResponseBody
	public ResponseDTO<?> modify(@RequestBody Post post) {
		postservice.updatePost(post);
		return new ResponseDTO<>(HttpStatus.OK.value(),post.getId()+"번 게시물 수정 완료");
	}
	
	@DeleteMapping("/post/{id}")
	@ResponseBody
	public ResponseDTO<?> delete(@PathVariable int id){
		postservice.deletePost(id);
		return new ResponseDTO<>(HttpStatus.OK.value(), id+"번 게시물 삭제 완료");
	}
}
