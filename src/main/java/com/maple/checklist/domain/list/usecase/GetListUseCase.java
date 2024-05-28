package com.maple.checklist.domain.list.usecase;

import com.maple.checklist.domain.list.dto.response.CheckListRes;
import com.maple.checklist.domain.member.entity.Member;

public interface GetListUseCase {
    CheckListRes getCheckList(Member member, Long characterId);
}
