package ru.epa.epabackend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TechnologyDto {
    private Long id;
    private String name;
}