package com.example.peer360.review.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "keyword_item")
public class KeywordItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String keywordName;

    @ManyToOne
    @JoinColumn(name = "review_id", referencedColumnName = "id", nullable = false)
    private Review review;
}
