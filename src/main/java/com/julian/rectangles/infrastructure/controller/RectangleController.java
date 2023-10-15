package com.julian.rectangles.infrastructure.controller;

import com.julian.rectangles.application.RectangleService;
import com.julian.rectangles.domain.dto.Coordinates;
import com.julian.rectangles.infrastructure.response.RectangleResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/rectangle")
public class RectangleController {

    private final RectangleService rectangleService;

    public RectangleController(RectangleService rectangleService) {
        this.rectangleService = rectangleService;
    }

    @Operation(summary = "Get adjacency, intersection and containment between two rectangles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RectangleResponse.class)) }),
            @ApiResponse(responseCode = "422", description = "Invalid coordinates",
                    content = @Content)
    })
    @PostMapping
    public RectangleResponse getRectangleResult(@Parameter(description = "Coordinates of the two rectangles.")
            @Valid @RequestBody Coordinates coordinates) {
        return this.rectangleService.getRectangleResult(coordinates);
    }

}
