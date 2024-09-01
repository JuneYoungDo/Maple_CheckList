package com.maple.checklist.domain.character.service;

import com.maple.checklist.domain.character.dto.request.CharacterDto;
import com.maple.checklist.domain.character.entity.Character;
import com.maple.checklist.domain.character.repository.CharacterRepository;
import com.maple.checklist.domain.character.usecase.UpdateCharacterUseCase;
import com.maple.checklist.domain.list.usecase.UpdateListUseCase;
import com.maple.checklist.domain.member.entity.Member;
import com.maple.checklist.global.config.exception.BaseException;
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
    private final GetCharacterService getCharacterService;
    private final UpdateListUseCase updateListUseCase;

    @Override
    public void updateAllCharacterInformation()
        throws IOException, ParseException, InterruptedException {
        List<Character> characterList = characterRepository.findAllByDeleted()
            .orElse(new ArrayList<>());
        for (Character character : characterList) {
            try {
                CharacterDto characterDto = mapleService.useGetCharacterInformation(
                    character.getOcid());
                character.updateInformation(characterDto);
            } catch (BaseException ignored) {
            }
        }
    }

    @Override
    public void deleteCharacterInformation(Member member, Long characterId) {
        Character character = getCharacterService.validateCharacter(member, characterId);
        updateListUseCase.deleteListByCharacterId(character);
        character.getAchievement().setDeleted();
        character.setDeleted();
    }

    @Override
    public void removeDeletedCharacters() {
        List<Character> characterList = characterRepository.findAllByDeletedTrue()
            .orElse(new ArrayList<>());
        characterRepository.deleteAll(characterList);
    }
}
