package com.maple.checklist.domain.character.entity;

import com.maple.checklist.domain.character.achievement.Achievement;
import com.maple.checklist.domain.character.dto.request.CharacterDto;
import com.maple.checklist.domain.list.entity.Daily;
import com.maple.checklist.domain.list.entity.Monthly;
import com.maple.checklist.domain.list.entity.Weekly;
import com.maple.checklist.domain.member.entity.Member;
import com.maple.checklist.global.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "characters")
public class Character extends BaseEntity {
    @Id
    @Column(name = "character_id",nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long characterId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "character_job", nullable = false)
    private String characterJob;

    @Column(name = "level", nullable = false)
    private Long level;

    @Column(name = "world", nullable = false)
    private String world;

    @Column(name = "ocid", nullable = false)
    private String ocid;

    @Column(name = "img", length = 500)
    private String img;

    @OneToOne
    @JoinColumn(name = "achievement")
    private Achievement achievement;

    @OneToMany(mappedBy = "character", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<Daily> dailies;
    @OneToMany(mappedBy = "character", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<Weekly> weeklies;
    @OneToMany(mappedBy = "character", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<Monthly> monthlies;

    public void updateAchievement(Achievement achievement) {
        this.achievement = achievement;
    }

    public void updateInformation(CharacterDto characterDto) {
        this.name = characterDto.getNickname();
        this.characterJob = characterDto.getJob();
        this.level = characterDto.getLevel();
        this.world = characterDto.getWorld();
        this.img = characterDto.getImg();
    }
}
