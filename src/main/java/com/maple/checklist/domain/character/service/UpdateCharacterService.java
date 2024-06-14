package com.maple.checklist.domain.character.service;

import com.maple.checklist.domain.character.dto.request.CharacterDto;
import com.maple.checklist.domain.character.entity.Character;
import com.maple.checklist.domain.character.repository.CharacterRepository;
import com.maple.checklist.domain.character.usecase.UpdateCharacterUseCase;
import com.maple.checklist.global.utils.MapleService;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateCharacterService implements UpdateCharacterUseCase {

    private final MapleService mapleService;
    private final CharacterRepository characterRepository;

    @Override
    public void updateAllCharacterInformation()
        throws IOException, ParseException, InterruptedException {
        List<Character> characterList = characterRepository.findAllByDeleted()
            .orElse(new ArrayList<>());
        for (Character character : characterList) {
            CharacterDto characterDto = mapleService.useGetCharacterInformation(
                character.getOcid());
            character.updateInformation(characterDto);
        }
    }
}
