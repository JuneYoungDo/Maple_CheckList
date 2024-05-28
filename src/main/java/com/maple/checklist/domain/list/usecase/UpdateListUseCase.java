package com.maple.checklist.domain.list.usecase;

import com.maple.checklist.domain.list.dto.request.CheckList;
import com.maple.checklist.domain.list.dto.request.CheckOne;
import com.maple.checklist.domain.member.entity.Member;

public interface UpdateListUseCase {
    void editCheckList(Member member, Long characterId, CheckList checkList);
    void checkOntItem(Member member, CheckOne checkOne);
}
