package org.example.smartfeedranking.entity.user;

import jakarta.persistence.*;
import lombok.*;
import org.example.smartfeedranking.entity.post.Post;

@Entity(name = "_user")
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
}
