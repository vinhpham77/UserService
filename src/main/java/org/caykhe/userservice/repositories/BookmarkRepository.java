package org.caykhe.userservice.repositories;


import jakarta.validation.constraints.NotNull;
import org.caykhe.userservice.models.Bookmark;
import org.caykhe.userservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Integer> {

    Optional<Bookmark> findByUsernameUsername(String username);
    Optional<Bookmark> findByUsername(@NotNull User username);

}
