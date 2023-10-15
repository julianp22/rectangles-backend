package com.julian.rectangles.application;

import com.julian.rectangles.domain.dto.Coordinates;
import com.julian.rectangles.domain.model.AdjacencyType;
import com.julian.rectangles.domain.model.Point;
import com.julian.rectangles.domain.model.Rectangle;
import com.julian.rectangles.infrastructure.response.RectangleResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.julian.rectangles.domain.model.Rectangle.isRectangleValid;

@Service
public class RectangleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RectangleService.class);
    private static final String INVALID_RECTANGLE_POINTS = "Invalid rectangle points. X1=%s and Y1=%s must be bottom left;"
            + " X2=%s and Y2=%s must be upper right.";

    /**
     * Calculates the adjacency, intersection and containment between two rectangles
     *
     * @param coordinates bottom left and upper right points of two rectangles
     * @return the {@code RectangleResponse} with adjacency, intersection and containment data
     */
    public RectangleResponse getRectangleResult(Coordinates coordinates) {
        Rectangle firstRectangle = buildRectangleFromCoordinates(coordinates.getR1x1(), coordinates.getR1x2(),
                coordinates.getR1y1(), coordinates.getR1y2());
        Rectangle secondRectangle = buildRectangleFromCoordinates(coordinates.getR2x1(), coordinates.getR2x2(),
                coordinates.getR2y1(), coordinates.getR2y2());

        LOGGER.info("Calculating adjacency, intersection and containment between: {} and {}", firstRectangle,
                secondRectangle);

        RectangleResponse rectangleResponse = new RectangleResponse();
        getAdjacency(rectangleResponse, firstRectangle, secondRectangle);
        getContainment(rectangleResponse, firstRectangle, secondRectangle);
        getIntersection(rectangleResponse, firstRectangle, secondRectangle);

        return rectangleResponse;
    }

    private void getAdjacency(RectangleResponse rectangleResponse, Rectangle firstRectangle, Rectangle secondRectangle) {
        rectangleResponse.setAdjacent(firstRectangle.isAdjacentTo(secondRectangle));

        if (rectangleResponse.isAdjacent()) {
            if (firstRectangle.hasProperAdjacencyWith(secondRectangle)) {
                rectangleResponse.setAdjacencyType(AdjacencyType.PROPER);
            } else if (firstRectangle.hasPartialAdjacencyWith(secondRectangle)) {
                rectangleResponse.setAdjacencyType(AdjacencyType.PARTIAL);
            } else if (firstRectangle.hasSublineAdjacencyWith(secondRectangle)) {
                rectangleResponse.setAdjacencyType(AdjacencyType.SUBLINE);
            }
        }
    }

    private void getContainment(RectangleResponse rectangleResponse, Rectangle firstRectangle,
                                Rectangle secondRectangle) {
        if (firstRectangle.containsRectangle(secondRectangle)) {
            rectangleResponse.setContained(true);
            rectangleResponse.setContainerRectangle(firstRectangle);
            rectangleResponse.setContaineeRectangle(secondRectangle);
        }
    }

    private void getIntersection(RectangleResponse rectangleResponse, Rectangle firstRectangle,
                                 Rectangle secondRectangle) {
        Optional<Rectangle> rectangleIntersection = firstRectangle.getIntersection(secondRectangle);

        if (rectangleIntersection.isPresent()) {
            rectangleResponse.setIntersected(true);
            rectangleResponse.setIntersection(rectangleIntersection.get());
        }
    }

    /**
     * Builds a new {@code Rectangle} by its diagonal coordinates
     *
     * @param x1 bottom left x
     * @param y1 bottom left y
     * @param x2 upper right x
     * @param y2 upper right y
     * @return the {@code Rectangle} represented by bottom left and upper right points
     */
    private Rectangle buildRectangleFromCoordinates(String x1, String x2, String y1, String y2) {
        float bottomLeftX = Float.parseFloat(x1);
        float bottomLeftY = Float.parseFloat(y1);
        float upperRightX = Float.parseFloat(x2);
        float upperRightY = Float.parseFloat(y2);

        Point bottomLeft = new Point(bottomLeftX, bottomLeftY);
        Point upperRight = new Point(upperRightX, upperRightY);

        validateRectangle(bottomLeft, upperRight);

        return new Rectangle(bottomLeft, upperRight);
    }

    /**
     * Builds a new {@code Rectangle} by its diagonal coordinates
     *
     * @param bottomLeft bottom left point
     * @param upperRight upper right point
     * @throws InvalidRectangleException if the points are not in correct position
     */
    private void validateRectangle(Point bottomLeft, Point upperRight) {
        if (!isRectangleValid(bottomLeft, upperRight)) {
            throw new InvalidRectangleException(String.format(INVALID_RECTANGLE_POINTS, bottomLeft.getX(),
                    bottomLeft.getY(), upperRight.getX(), upperRight.getY()));
        }
    }

}
