package com.maple.checklist.domain.list.usecase;

import com.maple.checklist.domain.character.entity.Character;
import com.maple.checklist.domain.list.dto.request.CheckList;
import com.maple.checklist.domain.list.dto.request.CheckOne;
import com.maple.checklist.domain.member.entity.Member;

public interface UpdateListUseCase {
    void editCheckList(Member member, Long characterId, CheckList checkList);
    void checkOntItem(Member member, CheckOne checkOne);
    void deleteListByCharacterId(Character character);
    void removeDeletedLists();

    void resetDaily();
    void resetWeekly();
    void resetMonthly();
}
