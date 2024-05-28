package com.maple.checklist.domain.character.controller;

import com.maple.checklist.domain.character.dto.request.CharacterDto;
import com.maple.checklist.domain.character.usecase.RegisterCharacterUseCase;
import com.maple.checklist.domain.member.entity.Member;
import com.maple.checklist.global.config.security.auth.CurrentMember;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/character")
public class RegisterCharacterController {

    private final RegisterCharacterUseCase registerCharacterUseCase;

    @PostMapping("")
    public ResponseEntity register(@CurrentMember Member member, @RequestBody CharacterDto characterDto) {
        registerCharacterUseCase.registerCharacter(member,characterDto);
        return new ResponseEntity(200, HttpStatus.OK);
    }

    @GetMapping("/nexon/info")
    public ResponseEntity getMapleCharacterInformation(@RequestParam String nickname)
        throws IOException, InterruptedException, ParseException {
        return new ResponseEntity(registerCharacterUseCase.getMapleCharacterInformation(nickname),
            HttpStatus.OK);
    }
}
