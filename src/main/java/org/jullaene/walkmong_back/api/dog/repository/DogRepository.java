package org.jullaene.walkmong_back.api.dog.repository;

import org.jullaene.walkmong_back.api.dog.domain.Dog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DogRepository extends JpaRepository<Dog, Long> {
}
