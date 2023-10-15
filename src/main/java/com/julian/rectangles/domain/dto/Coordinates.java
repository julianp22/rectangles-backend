package com.julian.rectangles.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Coordinates {

    @NotBlank(message = "Bottom left X of first rectangle is required.")
    private String r1x1;

    @NotBlank(message = "Upper right X of first rectangle is required.")
    private String r1x2;

    @NotBlank(message = "Bottom left Y of first rectangle is required.")
    private String r1y1;

    @NotBlank(message = "Upper right Y of first rectangle is required.")
    private String r1y2;

    @NotBlank(message = "Bottom left X of second rectangle is required.")
    private String r2x1;

    @NotBlank(message = "Upper right X of second rectangle is required.")
    private String r2x2;

    @NotBlank(message = "Bottom left Y of second rectangle is required.")
    private String r2y1;

    @NotBlank(message = "Upper right Y of second rectangle is required.")
    private String r2y2;

}
