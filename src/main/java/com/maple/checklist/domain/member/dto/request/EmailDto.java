package com.maple.checklist.domain.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmailDto {
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
}
