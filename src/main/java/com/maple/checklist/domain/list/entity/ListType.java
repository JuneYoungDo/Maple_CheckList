package com.maple.checklist.domain.list.entity;

import com.maple.checklist.global.config.exception.BaseException;
import com.maple.checklist.global.config.exception.errorCode.ListErrorCode;

public enum ListType {
    DAILY, WEEKLY, MONTHLY;

    public static ListType fromString(String type) {
        try {
            return ListType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BaseException(ListErrorCode.INVALID_LIST_TYPE);
        }
    }
}
