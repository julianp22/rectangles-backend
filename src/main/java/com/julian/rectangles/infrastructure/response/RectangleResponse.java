package com.julian.rectangles.infrastructure.response;

import com.julian.rectangles.domain.model.AdjacencyType;
import com.julian.rectangles.domain.model.Rectangle;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RectangleResponse {

    private boolean isAdjacent;
    private boolean isContained;
    private boolean isIntersected;
    private AdjacencyType adjacencyType;
    private Rectangle containerRectangle;
    private Rectangle containeeRectangle;
    private Rectangle intersection;

}
