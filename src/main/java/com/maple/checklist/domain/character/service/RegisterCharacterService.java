package com.maple.checklist.domain.character.service;

import com.maple.checklist.domain.character.achievement.AchievementService;
import com.maple.checklist.domain.character.dto.request.CharacterDto;
import com.maple.checklist.domain.character.dto.request.RegisterCharacterDto;
import com.maple.checklist.domain.character.entity.Character;
import com.maple.checklist.domain.character.repository.CharacterRepository;
import com.maple.checklist.domain.character.usecase.RegisterCharacterUseCase;
import com.maple.checklist.domain.list.dto.request.CheckItem;
import com.maple.checklist.domain.list.dto.request.CheckList;
import com.maple.checklist.domain.list.dto.request.ItemContent;
import com.maple.checklist.domain.list.dto.request.RegisterCheckList;
import com.maple.checklist.domain.list.service.UpdateListService;
import com.maple.checklist.domain.member.entity.Member;
import com.maple.checklist.global.utils.MapleService;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

@Transactional
@Service
@RequiredArgsConstructor
public class RegisterCharacterService implements RegisterCharacterUseCase {

    private final MapleService mapleService;
    private final CharacterRepository characterRepository;
    private final AchievementService achievementService;
    private final UpdateListService updateListService;

    private void save(Character character) {
        characterRepository.save(character);
    }

    private Character register(Member member, CharacterDto characterDto) {
        Character character = Character.builder()
            .member(member)
            .name(characterDto.getNickname())
            .characterJob(characterDto.getJob())
            .world(characterDto.getWorld())
            .level(characterDto.getLevel())
            .ocid(characterDto.getOcid())
            .img(characterDto.getImg())
            .build();
        achievementService.updateAchievement(character, 0L, 0L, 0L, 0L, 0L, 0L);
        save(character);
        return character;
    }

    @Override
    public CharacterDto getMapleCharacterInformation(String nickname)
        throws IOException, InterruptedException, ParseException {
        return mapleService.getMapleCharacterInformation(nickname);
    }

    @Override
    public void registerCharacter(Member member, RegisterCharacterDto registerCharacterDto) {
        Character character = register(member, registerCharacterDto.getCharacterDto());

        updateListService.editListAndUpdateAchievement(character,
            registerCheckListToCheckList(registerCharacterDto.getCheckList()));
    }

    private CheckList registerCheckListToCheckList(RegisterCheckList registerCheckList) {
        // dailyList 변환 처리
        List<CheckItem> dailyList = new ArrayList<>();
        if (registerCheckList.getDailyList() != null) {
            for (ItemContent item : registerCheckList.getDailyList()) {
                dailyList.add(new CheckItem(item.getContent(), false));
            }
        }

        // weeklyList 변환 처리
        List<CheckItem> weeklyList = new ArrayList<>();
        if (registerCheckList.getWeeklyList() != null) {
            for (ItemContent item : registerCheckList.getWeeklyList()) {
                weeklyList.add(new CheckItem(item.getContent(), false));
            }
        }

        // monthlyList 변환 처리
        List<CheckItem> monthlyList = new ArrayList<>();
        if (registerCheckList.getMonthlyList() != null) {
            for (ItemContent item : registerCheckList.getMonthlyList()) {
                monthlyList.add(new CheckItem(item.getContent(), false));
            }
        }

        // 새로운 CheckList 객체를 생성하여 반환
        return CheckList.builder()
            .dailyList(dailyList)
            .weeklyList(weeklyList)
            .monthlyList(monthlyList)
            .build();
    }

}
