package com.helmigandi.finshot_bulletin.repository;

import com.helmigandi.finshot_bulletin.dto.PostSummary;
import com.helmigandi.finshot_bulletin.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query("""
            SELECT NEW
                com.helmigandi.finshot_bulletin.dto.PostSummary(
                    p.id,
                    p.title,
                    p.authorName,
                    p.totalViews,
                    p.createdAt
                )
            FROM
                Post p
            WHERE
                p.isActive = true
            """)
    Page<PostSummary> findAllActivePostsSummary(Pageable pageable);

    @Modifying
    @Query("""
            UPDATE
                Post p
            SET
                p.isActive = :isActive,
                p.updatedAt = :updatedAt
            WHERE
                p.id = :id
            """)
    void setIsActiveById(Integer id, boolean isActive, OffsetDateTime updatedAt);

    @Query("""
            SELECT
                p.password
            FROM
                Post p
            WHERE
                p.id = :id
            """)
    Optional<String> findPasswordById(Integer id);

    @Modifying
    @Query("""
            UPDATE
                Post p
            SET
                p.totalViews = p.totalViews + 1
            WHERE
                p.id = :id AND
                p.isActive = true
            """)
    void setOneViewById(Integer id);
}
