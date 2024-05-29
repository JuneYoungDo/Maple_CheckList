package com.maple.checklist.domain.character.achievement;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "achievement")
public class Achievement {
    @Id
    @Column(name = "achievement_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long achievementId;

    @Column(name = "daily")
    private Long daily;
    @Column(name = "daily_complete")
    private Long dailyComplete;

    @Column(name = "weekly")
    private Long weekly;
    @Column(name = "weekly_complete")
    private Long weeklyComplete;

    @Column(name = "monthly")
    private Long monthly;
    @Column(name = "monthly_complte")
    private Long monthlyComplete;

    void achievement() {
        this.daily = 0L;
        this.dailyComplete = 0L;
        this.weekly = 0L;
        this.weeklyComplete = 0L;
        this.monthly = 0L;
        this.monthlyComplete = 0L;
    }

    public void update(Long daily, Long dailyComplete, Long weekly, Long weeklyComplete,
        Long monthly, Long monthlyComplete) {
        this.daily = daily;
        this.dailyComplete = dailyComplete;
        this.weekly = weekly;
        this.weeklyComplete = weeklyComplete;
        this.monthly = monthly;
        this.monthlyComplete = monthlyComplete;
    }

    public void updateDailyComplete(Boolean after) {
        if (this.dailyComplete == null) {
            this.dailyComplete = 0L;
        }
        if (after) {
            this.dailyComplete++;
        } else {
            this.dailyComplete--;
        }
    }

    public void updateWeeklyComplete(Boolean after) {
        if (this.weeklyComplete == null) {
            this.weeklyComplete = 0L;
        }
        if (after) {
            this.weeklyComplete++;
        } else {
            this.weeklyComplete--;
        }
    }

    public void updateMonthlyComplete(Boolean after) {
        if (this.monthlyComplete == null) {
            this.monthlyComplete = 0L;
        }
        if (after) {
            this.monthlyComplete++;
        } else {
            this.monthlyComplete--;
        }
    }
}
