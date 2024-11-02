package org.jullaene.walkmong_back.api.post.repository;

import org.jullaene.walkmong_back.api.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}
