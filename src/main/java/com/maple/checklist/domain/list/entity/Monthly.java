package com.maple.checklist.domain.list.entity;

import com.maple.checklist.domain.character.entity.Character;
import com.maple.checklist.global.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "monthly")
public class Monthly extends BaseEntity {
    @Id
    @Column(name = "monthly_id",nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long monthlyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id")
    private Character character;

    @Column(name = "content")
    private String content;

    @Column(name = "is_completed")
    private Boolean completed;
}
