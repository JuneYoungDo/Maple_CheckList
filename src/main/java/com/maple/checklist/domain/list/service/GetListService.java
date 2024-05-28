package com.maple.checklist.domain.list.service;

import com.maple.checklist.domain.character.entity.Character;
import com.maple.checklist.domain.character.repository.CharacterRepository;
import com.maple.checklist.domain.list.dto.response.CheckItemRes;
import com.maple.checklist.domain.list.dto.response.CheckListRes;
import com.maple.checklist.domain.list.repository.DailyRepository;
import com.maple.checklist.domain.list.repository.MonthlyRepository;
import com.maple.checklist.domain.list.repository.WeeklyRepository;
import com.maple.checklist.domain.list.usecase.GetListUseCase;
import com.maple.checklist.domain.member.entity.Member;
import com.maple.checklist.global.config.exception.BaseException;
import com.maple.checklist.global.config.exception.errorCode.CharacterErrorCode;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class GetListService implements GetListUseCase {

    private final CharacterRepository characterRepository;
    private final DailyRepository dailyRepository;
    private final WeeklyRepository weeklyRepository;
    private final MonthlyRepository monthlyRepository;

    @Override
    public CheckListRes getCheckList(Member member, Long characterId) {
        Character character = validateCharacterId(member, characterId);
        List<CheckItemRes> dailies = new ArrayList<>();
        List<CheckItemRes> weeklies = new ArrayList<>();
        List<CheckItemRes> monthlies = new ArrayList<>();
        dailyRepository.findAllByCharacterAndDeleted(character).orElse(new ArrayList<>()).forEach(
            daily -> dailies.add(
                new CheckItemRes(daily.getDailyId(), daily.getContent(), daily.getCompleted()))
        );
        weeklyRepository.findAllByCharacterAndDeleted(character).orElse(new ArrayList<>()).forEach(
            weekly -> weeklies.add(
                new CheckItemRes(weekly.getWeeklyId(), weekly.getContent(), weekly.getCompleted()))
        );
        monthlyRepository.findAllByCharacterAndDeleted(character).orElse(new ArrayList<>()).forEach(
            monthly -> monthlies.add(new CheckItemRes(monthly.getMonthlyId(), monthly.getContent(),
                monthly.getCompleted()))
        );

        return CheckListRes.builder()
            .dailyList(dailies)
            .weeklyList(weeklies)
            .monthlyList(monthlies)
            .build();
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
