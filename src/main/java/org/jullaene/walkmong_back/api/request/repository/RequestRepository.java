package org.jullaene.walkmong_back.api.request.repository;

import org.jullaene.walkmong_back.api.request.domain.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
}
