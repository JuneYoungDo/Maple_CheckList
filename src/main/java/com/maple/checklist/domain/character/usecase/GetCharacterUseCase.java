package com.maple.checklist.domain.character.usecase;

import com.maple.checklist.domain.character.dto.response.CharacterInformation;
import com.maple.checklist.domain.member.entity.Member;

public interface GetCharacterUseCase {
    CharacterInformation getCharacterInformation(Member member,Long characterId);
}
