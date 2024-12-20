package com.helmigandi.finshot_bulletin.controller;

import com.helmigandi.finshot_bulletin.dto.PostDeleteRequest;
import com.helmigandi.finshot_bulletin.dto.PostRequest;
import com.helmigandi.finshot_bulletin.dto.PostSummary;
import com.helmigandi.finshot_bulletin.model.Post;
import com.helmigandi.finshot_bulletin.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping
    public String listPosts(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size,
                            Model model) {
        Page<PostSummary> posts = postService.getAll(page, size);

        model.addAttribute("posts", posts);
        model.addAttribute("size", size);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", posts.getTotalPages());
        model.addAttribute("totalItems", posts.getTotalElements());
        return "index";
    }

    @GetMapping("/posts/{id}")
    public String getPost(@PathVariable Integer id, Model model) {
        Post post = postService.get(id);
        model.addAttribute("post", post);
        return "post-detail";
    }

    @GetMapping("/posts/create")
    public String showCreateForm(Model model) {
        model.addAttribute("postRequest", new PostRequest());
        model.addAttribute("isEdit", false);
        return "post-create";
    }

    @GetMapping("/posts/{id}/edit")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Post post = postService.get(id);
        PostRequest postRequest = new PostRequest();
        postRequest.setTitle(post.getTitle());
        postRequest.setContent(post.getContent());
        postRequest.setAuthorName(post.getAuthorName());

        model.addAttribute("postRequest", postRequest);
        model.addAttribute("postId", id);
        model.addAttribute("isEdit", true);
        return "post-create";
    }

    @PostMapping("/posts/create")
    public String createPost(@Valid @ModelAttribute("postRequest") PostRequest postRequest,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", false);
            return "post-create";
        }

        postService.create(postRequest);
        return "redirect:/";
    }

    @PostMapping("/posts/{id}/edit")
    public String updatePost(@PathVariable Integer id,
                             @Valid @ModelAttribute("postRequest") PostRequest postRequest,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("postId", id);
            model.addAttribute("isEdit", true);
            return "post-create";
        }

        postService.update(id, postRequest);
        return "redirect:/";
    }

    @PostMapping("/posts/{id}/delete")
    public String deletePost(@PathVariable Integer id,
                             @ModelAttribute PostDeleteRequest request) {
        postService.delete(id, request.getPostPassword());
        return "redirect:/";
    }

    @PostMapping("/posts/{id}/views")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> incrementViews(@PathVariable Integer id) {
        postService.incrementViews(id);
        return ResponseEntity.ok().body("OK");
    }
}
