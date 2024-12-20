package com.helmigandi.finshot_bulletin.service;

import com.helmigandi.finshot_bulletin.dto.PostRequest;
import com.helmigandi.finshot_bulletin.dto.PostSummary;
import com.helmigandi.finshot_bulletin.helper.BCrypt;
import com.helmigandi.finshot_bulletin.model.Post;
import com.helmigandi.finshot_bulletin.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    @Override
    public Page<PostSummary> getAll(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt")
                .and(Sort.by(Sort.Direction.DESC, "id"));

        PageRequest pageRequest = PageRequest.of(page, size, sort);

        Page<PostSummary> postsSummary = postRepository.findAllActivePostsSummary(pageRequest);
        List<PostSummary> postsSummaryModified = postsSummary
                .getContent().stream()
                .map(post -> {
                    if (isTitleKorean(post.getTitle()) && post.getTitle().length() > 50) {
                        post.setTitle(post.getTitle().substring(0, 50) + "...");
                    }
                    return post;
                }).toList();

        return new PageImpl<>(postsSummaryModified, pageRequest, postsSummary.getTotalElements());
    }

    @Override
    public Post get(Integer id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
    }

    @Override
    public void create(PostRequest request) {
        OffsetDateTime now = OffsetDateTime.now();
        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setAuthorName(request.getAuthorName());
        post.setActive(true);
        post.setTotalViews(0);
        post.setCreatedAt(now);
        post.setUpdatedAt(now);
        post.setPassword(BCrypt.hashpw(request.getPostPassword(), BCrypt.gensalt()));
        postRepository.save(post);
    }

    @Override
    public void update(Integer id, PostRequest request) {
        Post post = get(id);

        if (!BCrypt.checkpw(request.getPostPassword(), post.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Password wrong");

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setAuthorName(request.getAuthorName());
        post.setUpdatedAt(OffsetDateTime.now());
        postRepository.save(post);
    }

    @Override
    @Transactional
    public void delete(Integer id, String passRequest) {
        OffsetDateTime now = OffsetDateTime.now();

        String passExisting = postRepository.findPasswordById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        if (!BCrypt.checkpw(passRequest, passExisting))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Password wrong");

        postRepository.setIsActiveById(id, false, now);
    }

    @Override
    @Transactional
    public void incrementViews(Integer id) {
        postRepository.setOneViewById(id);
    }

    private boolean isTitleKorean(String title) {
        Pattern p = Pattern.compile("\\p{IsHangul}");
        Matcher m = p.matcher(title);
        return m.find();
    }
}
