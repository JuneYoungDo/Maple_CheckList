package com.maple.checklist.domain.list.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CheckItemRes {
    private Long listId;
    private String content;
    private Boolean completed;
}
