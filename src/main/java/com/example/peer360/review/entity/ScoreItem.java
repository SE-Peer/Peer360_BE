package com.example.peer360.review.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "score_item")
public class ScoreItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    private Integer score;

    @ManyToOne
    @JoinColumn(name = "review_id", referencedColumnName = "id", nullable = false)
    private Review review;
}