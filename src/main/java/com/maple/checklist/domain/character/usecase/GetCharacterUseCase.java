package com.maple.checklist.domain.character.usecase;

import com.maple.checklist.domain.character.dto.response.CharacterInformation;
import com.maple.checklist.domain.member.entity.Member;
import java.util.List;

public interface GetCharacterUseCase {
    List<CharacterInformation> getCharacterList(Member member);
    CharacterInformation getCharacterInformation(Member member,Long characterId);
}
