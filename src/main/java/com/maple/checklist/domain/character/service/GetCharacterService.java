package com.maple.checklist.domain.character.service;

import com.maple.checklist.domain.character.achievement.Achievement;
import com.maple.checklist.domain.character.dto.response.CharacterInformation;
import com.maple.checklist.domain.character.entity.Character;
import com.maple.checklist.domain.character.repository.CharacterRepository;
import com.maple.checklist.domain.character.usecase.GetCharacterUseCase;
import com.maple.checklist.domain.member.entity.Member;
import com.maple.checklist.global.config.exception.BaseException;
import com.maple.checklist.global.config.exception.errorCode.CharacterErrorCode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetCharacterService implements GetCharacterUseCase {

    private final CharacterRepository characterRepository;

    @Override
    public List<CharacterInformation> getCharacterList(Member member) {
        List<Character> characters = characterRepository.findCharactersByMember(member)
            .orElseGet(ArrayList::new);

        return characters.stream()
            .map(this::convertToCharacterInformation)
            .collect(Collectors.toList());
    }

    @Override
    public CharacterInformation getCharacterInformation(Member member, Long characterId) {
        Character character = validateCharacter(member,characterId);
        return convertToCharacterInformation(character);
    }

    private CharacterInformation convertToCharacterInformation(Character character) {
        Achievement achievement = character.getAchievement();
        return CharacterInformation.builder()
            .characterId(character.getCharacterId())
            .nickname(character.getName())
            .level(character.getLevel())
            .world(character.getWorld())
            .job(character.getCharacterJob().getName())
            .img(character.getCharacterJob().getImage())
            .dailyRate(calculateRate(achievement.getDaily(), achievement.getDailyComplete()))
            .weeklyRate(calculateRate(achievement.getWeekly(), achievement.getWeeklyComplete()))
            .monthlyRate(calculateRate(achievement.getMonthly(), achievement.getMonthlyComplete()))
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
