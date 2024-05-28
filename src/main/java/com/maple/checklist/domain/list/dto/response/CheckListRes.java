package com.maple.checklist.domain.list.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckListRes {
    private List<CheckItemRes> dailyList;
    private List<CheckItemRes> weeklyList;
    private List<CheckItemRes> monthlyList;
}
