package com.maple.checklist.domain.list.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckList {
    private List<CheckItem> dailyList;
    private List<CheckItem> weeklyList;
    private List<CheckItem> monthlyList;
}
