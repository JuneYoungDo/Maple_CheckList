package com.maple.checklist.domain.character.achievement;

import com.maple.checklist.domain.character.entity.Character;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AchievementService {

    private final AchievementRepository achievementRepository;

    private Achievement save(Achievement achievement) {
        achievementRepository.save(achievement);
        return achievement;
    }

    public void updateAchievement(Character character, Long daily, Long dailyComplete, Long weekly,
        Long weeklyComplete, Long monthly, Long monthlyComplete) {
        Achievement achievement;
        if (character.getAchievement() == null) {
            achievement = save(Achievement.builder()
                .daily(daily)
                .dailyComplete(dailyComplete)
                .weekly(weekly)
                .weeklyComplete(weeklyComplete)
                .monthly(monthly)
                .monthlyComplete(monthlyComplete)
                .build()
            );
        } else {
            achievement = character.getAchievement();
            achievement.update(daily, dailyComplete, weekly, weeklyComplete, monthly,
                monthlyComplete);
        }
        character.updateAchievement(achievement);
    }

    public void resetAchievement(String type) {
        List<Achievement> achievementList = achievementRepository.findAllByDeleted()
            .orElse(new ArrayList<>());
        achievementList.forEach(achievement -> achievement.resetAchievement(type));
    }
}
