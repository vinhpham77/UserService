package org.caykhe.userservice.repositories;

import org.caykhe.userservice.models.Authentication;
import org.caykhe.userservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthenticationRepository extends JpaRepository<Authentication, Integer> {
    Optional<Authentication> findByUsername(User user);

    void deleteByUsername(User user);

}
