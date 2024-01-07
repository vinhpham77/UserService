package org.caykhe.userservice.models;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@IdClass(FollowId.class)
@Table(name = "follows")
public class Follow {
    @Id
    @ManyToOne
    @JoinColumn(name = "follower", referencedColumnName = "username")
    private User follower;

    @Id
    @ManyToOne
    @JoinColumn(name = "followed", referencedColumnName = "username")
    private User followed;

}