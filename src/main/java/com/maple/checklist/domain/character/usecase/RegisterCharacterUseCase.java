package com.maple.checklist.domain.character.usecase;

import com.maple.checklist.domain.character.dto.request.CharacterDto;
import com.maple.checklist.domain.character.dto.request.RegisterCharacterDto;
import com.maple.checklist.domain.member.entity.Member;
import java.io.IOException;
import org.json.simple.parser.ParseException;

public interface RegisterCharacterUseCase {
    CharacterDto getMapleCharacterInformation(String nickname)
        throws IOException, InterruptedException, ParseException;

    void registerCharacter(Member member, RegisterCharacterDto registerCharacterDto);
}
