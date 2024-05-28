package com.maple.checklist.domain.character.controller;

import com.maple.checklist.domain.character.usecase.GetCharacterUseCase;
import com.maple.checklist.domain.member.entity.Member;
import com.maple.checklist.global.config.security.auth.CurrentMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/character")
public class GetCharacterController {

    private final GetCharacterUseCase getCharacterUseCase;

    @GetMapping("")
    public ResponseEntity getCharacterInformation(@CurrentMember Member member,
        @RequestParam Long characterId) {
        return new ResponseEntity(getCharacterUseCase.getCharacterInformation(member,characterId),
            HttpStatus.OK);
    }
}
