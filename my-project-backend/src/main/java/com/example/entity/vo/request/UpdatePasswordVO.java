package com.example.entity.vo.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordVO {
    @Size(max = 20)
    String oldPassword;
    @Size(max = 20)
    String newPassword;
}
