package com.maple.checklist.domain.character.usecase;

import com.maple.checklist.domain.member.entity.Member;
import java.io.IOException;
import org.json.simple.parser.ParseException;

public interface UpdateCharacterUseCase {

    void updateAllCharacterInformation() throws IOException, ParseException, InterruptedException;

    void deleteCharacterInformation(Member member,Long characterId);
}
