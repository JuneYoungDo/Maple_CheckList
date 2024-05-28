package com.maple.checklist.domain.list.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CheckItem {
    private String content;
    private Boolean completed;
}
