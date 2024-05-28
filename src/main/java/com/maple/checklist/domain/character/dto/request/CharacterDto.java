package com.maple.checklist.domain.character.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CharacterDto {
    private String nickname;
    private Long level;
    private String world;
    private String job;
}
