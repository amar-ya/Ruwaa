package org.example.ruwaa.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Post
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String content;

    private Integer views;

    private String type;

    @ManyToOne
    @JsonIgnore
    private Users users;

    @OneToMany(cascade=CascadeType.ALL,mappedBy = "post")
    @JsonIgnore
    private List<Review> reviews;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "post")
    private List<Attachments> attachments;
}
