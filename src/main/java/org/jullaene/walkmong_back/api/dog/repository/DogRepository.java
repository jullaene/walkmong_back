package org.jullaene.walkmong_back.api.dog.repository;

import org.jullaene.walkmong_back.api.dog.domain.Dog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DogRepository extends JpaRepository<Dog, Long> {
    Optional<Dog> findByDogIdAndDelYn(Long dogId, String delYn);
}
