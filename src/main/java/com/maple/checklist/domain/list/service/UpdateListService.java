package com.maple.checklist.domain.list.service;

import com.maple.checklist.domain.character.achievement.Achievement;
import com.maple.checklist.domain.character.achievement.AchievementService;
import com.maple.checklist.domain.character.entity.Character;
import com.maple.checklist.domain.character.repository.CharacterRepository;
import com.maple.checklist.domain.list.dto.request.CheckItem;
import com.maple.checklist.domain.list.dto.request.CheckList;
import com.maple.checklist.domain.list.dto.request.CheckOne;
import com.maple.checklist.domain.list.entity.Daily;
import com.maple.checklist.domain.list.entity.ListType;
import com.maple.checklist.domain.list.entity.Monthly;
import com.maple.checklist.domain.list.entity.Weekly;
import com.maple.checklist.domain.list.repository.DailyRepository;
import com.maple.checklist.domain.list.repository.MonthlyRepository;
import com.maple.checklist.domain.list.repository.WeeklyRepository;
import com.maple.checklist.domain.list.usecase.UpdateListUseCase;
import com.maple.checklist.domain.member.entity.Member;
import com.maple.checklist.global.BaseEntity;
import com.maple.checklist.global.config.exception.BaseException;
import com.maple.checklist.global.config.exception.errorCode.CharacterErrorCode;
import com.maple.checklist.global.config.exception.errorCode.ListErrorCode;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UpdateListService implements UpdateListUseCase {

    private final CharacterRepository characterRepository;
    private final DailyRepository dailyRepository;
    private final WeeklyRepository weeklyRepository;
    private final MonthlyRepository monthlyRepository;
    private final AchievementService achievementService;

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
    public void editCheckList(Member member, Long characterId, CheckList checkList) {
        Character character = validateCharacterId(member, characterId);
        setCheckListDeleted(character);
        editListAndUpdateAchievement(character, checkList);
    }

    @Override
    public void checkOntItem(Member member, CheckOne checkOne) {
        ListType checkType = ListType.fromString(checkOne.getType());
        Achievement achievement;
        Boolean after;

        switch (checkType) {
            case DAILY -> {
                Daily daily = validateDaily(member, checkOne);
                after = daily.updateCompleted();
                achievement = daily.getCharacter().getAchievement();
            }
            case WEEKLY -> {
                Weekly weekly = validateWeekly(member, checkOne);
                after = weekly.updateCompleted();
                achievement = weekly.getCharacter().getAchievement();
            }
            case MONTHLY -> {
                Monthly monthly = validateMonthly(member, checkOne);
                after = monthly.updateCompleted();
                achievement = monthly.getCharacter().getAchievement();
            }
            default -> throw new BaseException(ListErrorCode.INVALID_LIST_TYPE);
        }
        updateAchievement(achievement, checkType, after);
    }

    @Override
    public void resetDaily() {
        List<Daily> dailyList = dailyRepository.findAllByDeleted().orElse(new ArrayList<>());
        log.info("==== Daily   Reset ====");
        dailyList.forEach(daily -> {
            boolean before = daily.getCompleted();
            daily.resetCompleted();
            log.info("==== Daily Id: {}, before completed: {}, after completed: {} ====",
                daily.getDailyId(), before, daily.getCompleted());
        });
        log.info("=======================");
        achievementService.resetAchievement(ListType.DAILY.name());
    }

    @Override
    public void resetWeekly() {
        List<Weekly> weeklyList = weeklyRepository.findAllByDeleted().orElse(new ArrayList<>());
        log.info("==== Weekly  Reset ====");
        weeklyList.forEach(weekly -> {
            boolean before = weekly.getCompleted();
            weekly.resetCompleted();
            log.info("==== Weekly Id: {}, before completed: {}, after completed: {} ====",
                weekly.getWeeklyId(), before, weekly.getCompleted());
        });
        log.info("=======================");
        achievementService.resetAchievement(ListType.WEEKLY.name());
    }

    @Override
    public void resetMonthly() {
        List<Monthly> monthList = monthlyRepository.findAllByDeleted().orElse(new ArrayList<>());
        log.info("==== Monthly Reset ====");
        monthList.forEach(monthly -> {
            boolean before = monthly.getCompleted();
            monthly.resetCompleted();
            log.info("==== Monthly Id: {}, before completed: {}, after completed: {} ====",
                monthly.getMonthlyId(), before, monthly.getCompleted());
        });
        log.info("=======================");
        achievementService.resetAchievement(ListType.MONTHLY.name());
    }

    private void updateAchievement(Achievement achievement, ListType listType, Boolean after) {
        switch (listType) {
            case DAILY -> achievement.updateDailyComplete(after);
            case WEEKLY -> achievement.updateWeeklyComplete(after);
            case MONTHLY -> achievement.updateMonthlyComplete(after);
            default -> throw new BaseException(ListErrorCode.INVALID_LIST_TYPE);
        }
    }

    private Daily validateDaily(Member member, CheckOne checkOne) {
        Daily daily = dailyRepository.findDailyByDailyIdAndDeleted(checkOne.getListId())
            .orElseThrow(() -> new BaseException(ListErrorCode.INVALID_LIST_ID));
        if (!Objects.equals(daily.getCharacter().getMember().getMemberId(), member.getMemberId())) {
            throw new BaseException(ListErrorCode.INVALID_LIST);
        }
        return daily;
    }

    private Weekly validateWeekly(Member member, CheckOne checkOne) {
        Weekly weekly = weeklyRepository.findWeeklyByWeeklyIdAndDeleted(checkOne.getListId())
            .orElseThrow(() -> new BaseException(ListErrorCode.INVALID_LIST_ID));
        if (!Objects.equals(weekly.getCharacter().getMember().getMemberId(),
            member.getMemberId())) {
            throw new BaseException(ListErrorCode.INVALID_LIST);
        }
        return weekly;
    }

    private Monthly validateMonthly(Member member, CheckOne checkOne) {
        Monthly monthly = monthlyRepository.findMonthlyByMonthlyIdAndDeleted(checkOne.getListId())
            .orElseThrow(() -> new BaseException(ListErrorCode.INVALID_LIST_ID));
        if (!Objects.equals(monthly.getCharacter().getMember().getMemberId(),
            member.getMemberId())) {
            throw new BaseException(ListErrorCode.INVALID_LIST);
        }
        return monthly;
    }

    public void deleteListByCharacterId(Character character) {
        List<Daily> dailyList = dailyRepository.findAllByCharacterAndDeleted(character)
            .orElse(new ArrayList<>());
        List<Weekly> weeklyList = weeklyRepository.findAllByCharacterAndDeleted(character)
            .orElse(new ArrayList<>());
        List<Monthly> monthlyList = monthlyRepository.findAllByCharacterAndDeleted(character)
            .orElse(new ArrayList<>());
        for (Daily daily : dailyList) {
            daily.setDeleted();
        }
        for (Weekly weekly : weeklyList) {
            weekly.setDeleted();
        }
        for (Monthly monthly : monthlyList) {
            monthly.setDeleted();
        }
    }

    public void editListAndUpdateAchievement(Character character, CheckList checkList) {
        long daily, dailyComplete, weekly, weeklyComplete, monthly, monthlyComplete;
        List<CheckItem> dailyList = checkList.getDailyList();
        List<CheckItem> weeklyList = checkList.getWeeklyList();
        List<CheckItem> monthlyList = checkList.getMonthlyList();

        dailyList.forEach(item -> saveDailyFromCheckItem(item, character));
        daily = dailyList.size();
        dailyComplete = countCompletedItems(dailyList);

        weeklyList.forEach(item -> saveWeeklyFromCheckItem(item, character));
        weekly = weeklyList.size();
        weeklyComplete = countCompletedItems(weeklyList);

        monthlyList.forEach(item -> saveMonthlyFromCheckItem(item, character));
        monthly = monthlyList.size();
        monthlyComplete = countCompletedItems(monthlyList);

        achievementService.updateAchievement(character, daily, dailyComplete, weekly,
            weeklyComplete, monthly, monthlyComplete);
    }

    private void saveDailyFromCheckItem(CheckItem item, Character character) {
        Daily daily = Daily.builder()
            .character(character)
            .content(item.getContent())
            .completed(item.getCompleted())
            .build();
        saveDaily(daily);
    }

    private void saveWeeklyFromCheckItem(CheckItem item, Character character) {
        Weekly weekly = Weekly.builder()
            .character(character)
            .content(item.getContent())
            .completed(item.getCompleted())
            .build();
        saveWeekly(weekly);
    }

    private void saveMonthlyFromCheckItem(CheckItem item, Character character) {
        Monthly monthly = Monthly.builder()
            .character(character)
            .content(item.getContent())
            .completed(item.getCompleted())
            .build();
        saveMonthly(monthly);
    }

    private long countCompletedItems(List<CheckItem> list) {
        return list.stream().filter(CheckItem::getCompleted).count();
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
