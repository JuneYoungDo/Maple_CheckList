package com.maple.checklist.domain.list.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterCheckList {
    private List<ItemContent> dailyList;
    private List<ItemContent> weeklyList;
    private List<ItemContent> monthlyList;
}

