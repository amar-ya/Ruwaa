package org.example.ruwaa.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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
public class Media
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String content;

    private Integer views;

    @ManyToOne
    @JsonIgnore
    private Users users;

    @OneToMany(cascade=CascadeType.ALL,mappedBy = "media")
    @JsonIgnore
    private List<Review> reviews;
}
