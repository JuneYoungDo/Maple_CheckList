package com.maple.checklist.domain.character.service;

import com.maple.checklist.domain.character.achievement.Achievement;
import com.maple.checklist.domain.character.dto.response.CharacterInformation;
import com.maple.checklist.domain.character.entity.Character;
import com.maple.checklist.domain.character.repository.CharacterRepository;
import com.maple.checklist.domain.character.usecase.GetCharacterUseCase;
import com.maple.checklist.domain.member.entity.Member;
import com.maple.checklist.global.config.exception.BaseException;
import com.maple.checklist.global.config.exception.errorCode.CharacterErrorCode;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetCharacterService implements GetCharacterUseCase {

    private final CharacterRepository characterRepository;

    @Override
    public CharacterInformation getCharacterInformation(Member member, Long characterId) {
        Character character = validateCharacter(member,characterId);
        Achievement achievement = character.getAchievement();
        return CharacterInformation.builder()
            .nickname(character.getName())
            .level(character.getLevel())
            .world(character.getWorld())
            .job(character.getJob().getName())
            .img(character.getJob().getImage())
            .dailyRate(calculateRate(achievement.getDaily(),achievement.getDailyComplete()))
            .weeklyRate(calculateRate(achievement.getWeekly(),achievement.getWeeklyComplete()))
            .monthlyRate(calculateRate(achievement.getMonthly(),achievement.getMonthlyComplete()))
            .build();
    }

    private Long calculateRate(Long total, Long completed) {
        double percent = (double) completed * 100 / total;
        return Math.round(percent);
    }

    private Character validateCharacter(Member member, Long characterId) {
        Character character = characterRepository.findCharacterByCharacterIdAndDeleted(characterId)
            .orElseThrow(() -> new BaseException(CharacterErrorCode.INVALID_CHARACTER_ID));
        if (!Objects.equals(character.getMember().getMemberId(), member.getMemberId())) {
            throw new BaseException(CharacterErrorCode.INVALID_CHARACTER);
        }
        return character;
    }
}
