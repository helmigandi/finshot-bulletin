package com.helmigandi.finshot_bulletin.service;

import com.helmigandi.finshot_bulletin.dto.PostRequest;
import com.helmigandi.finshot_bulletin.dto.PostSummary;
import com.helmigandi.finshot_bulletin.model.Post;
import org.springframework.data.domain.Page;

public interface PostService {
    Page<PostSummary> getAll(int page, int size);

    Post get(Integer id);

    void create(PostRequest request);

    void update(Integer id, PostRequest request);

    void delete(Integer id, String passRequest);

    void incrementViews(Integer id);
}
