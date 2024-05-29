package com.maple.checklist.domain.character.service;

import com.maple.checklist.domain.character.achievement.Achievement;
import com.maple.checklist.domain.character.achievement.AchievementService;
import com.maple.checklist.domain.character.dto.request.CharacterDto;
import com.maple.checklist.domain.character.entity.Character;
import com.maple.checklist.domain.character.job.JobRepository;
import com.maple.checklist.domain.character.repository.CharacterRepository;
import com.maple.checklist.domain.character.usecase.RegisterCharacterUseCase;
import com.maple.checklist.domain.member.entity.Member;
import com.maple.checklist.global.utils.MapleService;
import jakarta.transaction.Transactional;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

@Transactional
@Service
@RequiredArgsConstructor
public class RegisterCharacterService implements RegisterCharacterUseCase {

    private final MapleService mapleService;
    private final CharacterRepository characterRepository;
    private final JobRepository jobRepository;
    private final AchievementService achievementService;

    private void save(Character character) {
        characterRepository.save(character);
    }

    private void register(Member member, CharacterDto characterDto) {
        Character character = Character.builder()
            .member(member)
            .name(characterDto.getNickname())
            .level(characterDto.getLevel())
            .world(characterDto.getWorld())
            .job(jobRepository.findJobByName(characterDto.getJob()).orElse(
                jobRepository.findDefaultJob()
            ))
            .build();
        achievementService.updateAchievement(character, 0L, 0L, 0L, 0L, 0L, 0L);
        save(character);
    }

    @Override
    public CharacterDto getMapleCharacterInformation(String nickname)
        throws IOException, InterruptedException, ParseException {
        return mapleService.getMapleCharacterInformation(nickname);
    }

    @Override
    public void registerCharacter(Member member, CharacterDto characterDto) {
        register(member, characterDto);
    }
}
