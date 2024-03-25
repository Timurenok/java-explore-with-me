package ru.practicum.ewm.user.model.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserOutDto {

    private Long id;
    private String email;
    private String name;
}
