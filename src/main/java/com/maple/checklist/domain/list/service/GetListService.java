package com.maple.checklist.domain.list.service;

import com.maple.checklist.domain.character.entity.Character;
import com.maple.checklist.domain.character.repository.CharacterRepository;
import com.maple.checklist.domain.list.dto.CheckItem;
import com.maple.checklist.domain.list.dto.CheckList;
import com.maple.checklist.domain.list.entity.Daily;
import com.maple.checklist.domain.list.entity.Monthly;
import com.maple.checklist.domain.list.entity.Weekly;
import com.maple.checklist.domain.list.repository.DailyRepository;
import com.maple.checklist.domain.list.repository.MonthlyRepository;
import com.maple.checklist.domain.list.repository.WeeklyRepository;
import com.maple.checklist.domain.list.usecase.GetListUseCase;
import com.maple.checklist.domain.member.entity.Member;
import com.maple.checklist.global.BaseEntity;
import com.maple.checklist.global.config.exception.BaseException;
import com.maple.checklist.global.config.exception.errorCode.CharacterErrorCode;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class GetListService implements GetListUseCase {

    private final CharacterRepository characterRepository;
    private final DailyRepository dailyRepository;
    private final WeeklyRepository weeklyRepository;
    private final MonthlyRepository monthlyRepository;

    private void saveDaily(Daily daily) {
        dailyRepository.save(daily);
    }
    private void saveWeekly(Weekly weekly) {
        weeklyRepository.save(weekly);
    }
    private void saveMonthly(Monthly monthly) {
        monthlyRepository.save(monthly);
    }

    @Override
    public CheckList getCheckList(Member member, Long characterId) {
        Character character = validateCharacterId(member, characterId);
        List<CheckItem> dailies = new ArrayList<>();
        List<CheckItem> weeklies = new ArrayList<>();
        List<CheckItem> monthlies = new ArrayList<>();
        dailyRepository.findAllByCharacterAndDeleted(character).orElse(new ArrayList<>()).forEach(
            daily -> dailies.add(new CheckItem(daily.getContent(), daily.getCompleted()))
        );
        weeklyRepository.findAllByCharacterAndDeleted(character).orElse(new ArrayList<>()).forEach(
            weekly -> weeklies.add(new CheckItem(weekly.getContent(), weekly.getCompleted()))
        );
        monthlyRepository.findAllByCharacterAndDeleted(character).orElse(new ArrayList<>()).forEach(
            monthly -> monthlies.add(new CheckItem(monthly.getContent(), monthly.getCompleted()))
        );

        return CheckList.builder()
            .dailyList(dailies)
            .weeklyList(weeklies)
            .monthlyList(monthlies)
            .build();
    }

    @Override
    public void editCheckList(Member member, Long characterId, CheckList checkList) {
        Character character = validateCharacterId(member, characterId);
        setCheckListDeleted(character);
        checkList.getDailyList().forEach( item -> saveDaily(Daily.builder()
            .character(character)
            .content(item.getContent())
            .completed(item.getCompleted())
            .build()));
        checkList.getWeeklyList().forEach(item -> saveWeekly(Weekly.builder()
            .character(character)
            .content(item.getContent())
            .completed(item.getCompleted())
            .build()));
        checkList.getMonthlyList().forEach(item -> saveMonthly(Monthly.builder()
            .character(character)
            .content(item.getContent())
            .completed(item.getCompleted())
            .build()));
    }



    private void setCheckListDeleted(Character character) {
        dailyRepository.findAllByCharacterAndDeleted(character).orElse(new ArrayList<>()).forEach(
            BaseEntity::setDeleted
        );
        weeklyRepository.findAllByCharacterAndDeleted(character).orElse(new ArrayList<>()).forEach(
            BaseEntity::setDeleted
        );
        monthlyRepository.findAllByCharacterAndDeleted(character).orElse(new ArrayList<>()).forEach(
            BaseEntity::setDeleted
        );
    }

    private Character validateCharacterId(Member member, Long characterId) {
        Character character = characterRepository.findCharacterByCharacterIdAndDeleted(characterId)
            .orElseThrow(() -> new BaseException(CharacterErrorCode.INVALID_CHARACTER_ID));
        if (!Objects.equals(character.getMember().getMemberId(), member.getMemberId())) {
            throw new BaseException(CharacterErrorCode.INVALID_CHARACTER);
        }
        return character;
    }
}
