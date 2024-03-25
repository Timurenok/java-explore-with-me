package ru.practicum.ewm.user.model.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserInDto {

    @Email
    @NotBlank
    @Size(min = 6, max = 254, message = "The length of the email must be from 6 to 254")
    private String email;
    @NotBlank
    @Size(min = 2, max = 250, message = "The length of the name must be from 2 to 250")
    private String name;
}
