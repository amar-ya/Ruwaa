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
public class Expert
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String category;

    private String about_me;

    private String Linkedin_url;

    private Integer review_count;

    private Double consult_price;


    @OneToOne(cascade = CascadeType.ALL, mappedBy = "expert")
    @PrimaryKeyJoinColumn
    private Resume resume;


    @OneToOne
    @MapsId
    @JsonIgnore
    private Users users;

    @OneToOne
    @JsonIgnore
    private Review review;
}
