package com.maple.checklist.domain.list.usecase;

import com.maple.checklist.domain.list.dto.CheckList;
import com.maple.checklist.domain.member.entity.Member;

public interface GetListUseCase {
    CheckList getCheckList(Member member, Long characterId);
    void editCheckList(Member member, Long characterId, CheckList checkList);
}
