package org.example.ruwaa.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Review
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String content;

    private Integer feedback_rating;

    private String status;

    private Boolean hasRated;
    private Integer rate;

    @OneToOne(mappedBy = "review")
    @JsonIgnore
    private Expert expert;

    @ManyToOne
    @JsonIgnore
    private Post post;

    @OneToOne(cascade = CascadeType.ALL,mappedBy = "reviewChat")
    @PrimaryKeyJoinColumn
    @JsonIgnore
    private Chat chat;


}
