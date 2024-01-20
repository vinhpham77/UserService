package org.caykhe.userservice.repositories;


import org.caykhe.userservice.models.User;
import org.caykhe.userservice.models.Vote;
import org.caykhe.userservice.models.VoteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, VoteId> {
    Optional<Vote> findByTargetIdAndTargetTypeAndUser(Integer targetId, Boolean targetType, User user);
    @Modifying
    @Query("DELETE FROM Vote v WHERE v.targetId = :targetId AND v.targetType = :targetType AND v.user = :user")
    int deleteByTargetIdAndTargetTypeAndUser(
            @Param("targetId") Integer targetId,
            @Param("targetType") Boolean targetType,
            @Param("user") User user
    );
}
