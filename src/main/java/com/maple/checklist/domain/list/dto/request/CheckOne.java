package com.maple.checklist.domain.list.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CheckOne {
    private String type;
    private Long listId;
}
