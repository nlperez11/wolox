package com.wolox.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@Entity(name = "album_access")
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"album_id", "user_id"})
)
public class AlbumAccess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "album_id", referencedColumnName = "id")
    private Album album;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "write", nullable = false)
    private boolean write;

    @Column(name = "read", nullable = false)
    private boolean read;
}
