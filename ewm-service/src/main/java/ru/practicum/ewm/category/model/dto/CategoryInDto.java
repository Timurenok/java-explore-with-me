package ru.practicum.ewm.category.model.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CategoryInDto {

    @NotBlank
    @Size(min = 1, max = 50, message = "The length of the category name must be from 1 to 50")
    private String name;
}
