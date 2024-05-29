package com.maple.checklist.domain.character.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CharacterInformation {
    private Long characterId;
    private String nickname;
    private Long level;
    private String world;
    private String job;
    private String img;
    private Long dailyRate;
    private Long weeklyRate;
    private  Long monthlyRate;
}
