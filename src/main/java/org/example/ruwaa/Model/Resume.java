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
public class Resume
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;



    @OneToOne
    @MapsId
    @JsonIgnore
    private Expert expert;


}
