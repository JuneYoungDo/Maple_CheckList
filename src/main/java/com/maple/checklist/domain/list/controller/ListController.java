package com.maple.checklist.domain.list.controller;

import com.maple.checklist.domain.list.dto.request.CheckList;
import com.maple.checklist.domain.list.dto.request.CheckOne;
import com.maple.checklist.domain.list.usecase.GetListUseCase;
import com.maple.checklist.domain.list.usecase.UpdateListUseCase;
import com.maple.checklist.domain.member.entity.Member;
import com.maple.checklist.global.config.security.auth.CurrentMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/list")
public class ListController {

    private final GetListUseCase getListUseCase;
    private final UpdateListUseCase updateListUseCase;

    @GetMapping("")
    public ResponseEntity getCharacterCheckList(@CurrentMember Member member,
        @RequestParam Long characterId) {
        return new ResponseEntity(getListUseCase.getCheckList(member, characterId), HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity editCharacterCheckList(@CurrentMember Member member,
        @RequestParam Long characterId, @RequestBody CheckList checkList) {
        updateListUseCase.editCheckList(member, characterId, checkList);
        return new ResponseEntity(200, HttpStatus.OK);
    }

    @PatchMapping("")
    public ResponseEntity checkOneItem(@CurrentMember Member member,
        @RequestBody CheckOne checkOne) {
        updateListUseCase.checkOntItem(member, checkOne);
        return new ResponseEntity(200, HttpStatus.OK);
    }
}
