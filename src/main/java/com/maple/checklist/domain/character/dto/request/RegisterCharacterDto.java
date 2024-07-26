package com.maple.checklist.domain.character.dto.request;

import com.maple.checklist.domain.list.dto.request.CheckList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterCharacterDto {
    private CharacterDto characterDto;
    private CheckList checkList;
}
