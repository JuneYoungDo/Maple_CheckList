package com.maple.checklist.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GetMemberController {

    @GetMapping("/")
    public String index() {
        System.out.println();
        return "";
    }


}
