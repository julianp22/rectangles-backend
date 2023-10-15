package com.julian.rectangles.application;

import com.julian.rectangles.domain.dto.Coordinates;
import com.julian.rectangles.domain.model.AdjacencyType;
import com.julian.rectangles.domain.model.Point;
import com.julian.rectangles.domain.model.Rectangle;
import com.julian.rectangles.infrastructure.response.RectangleResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class RectangleServiceTest {

    private RectangleService rectangleService;

    @BeforeEach
    void setup() {
        rectangleService = new RectangleService();
    }

    @ParameterizedTest
    @MethodSource("testIntersectionParameters")
    void testRectangleIntersectionSuccess(String r1x1, String r1x2, String r1y1, String r1y2,
                                          String r2x1, String r2x2, String r2y1, String r2y2,
                                          Rectangle intersection) {
        // Arrange
        Coordinates coordinates = new Coordinates(r1x1, r1x2, r1y1, r1y2, r2x1, r2x2, r2y1, r2y2);

        // Act
        RectangleResponse response = rectangleService.getRectangleResult(coordinates);

        // Assert
        assertTrue(response.isIntersected());
        assertEquals(intersection, response.getIntersection());
        assertFalse(response.isContained());
        assertFalse(response.isAdjacent());
    }

    @ParameterizedTest
    @MethodSource("testContainmentParameters")
    void testRectangleContainmentSuccess(String r1x1, String r1x2, String r1y1, String r1y2,
                                         String r2x1, String r2x2, String r2y1, String r2y2,
                                         Rectangle container, Rectangle containee) {
        // Arrange
        Coordinates coordinates = new Coordinates(r1x1, r1x2, r1y1, r1y2, r2x1, r2x2, r2y1, r2y2);

        // Act
        RectangleResponse response = rectangleService.getRectangleResult(coordinates);

        // Assert
        assertTrue(response.isContained());
        assertNotNull(response.getContainerRectangle());
        assertNotNull(response.getContaineeRectangle());
        assertEquals(container, response.getContainerRectangle());
        assertEquals(containee, response.getContaineeRectangle());
        assertTrue(response.isIntersected());
        assertFalse(response.isAdjacent());
    }

    @ParameterizedTest
    @MethodSource("testAdjacencyParameters")
    void testRectangleAdjacencySuccess(String r1x1, String r1x2, String r1y1, String r1y2,
                                       String r2x1, String r2x2, String r2y1, String r2y2,
                                       AdjacencyType adjacencyType) {
        // Arrange
        Coordinates coordinates = new Coordinates(r1x1, r1x2, r1y1, r1y2, r2x1, r2x2, r2y1, r2y2);

        // Act
        RectangleResponse response = rectangleService.getRectangleResult(coordinates);

        // Assert
        assertTrue(response.isAdjacent());
        assertEquals(adjacencyType, response.getAdjacencyType());
        assertFalse(response.isIntersected());
        assertFalse(response.isContained());
    }

    @ParameterizedTest
    @MethodSource("testRectangleContainmentAdjacencyIntersectionFalse")
    void testRectangleContainmentAdjacencyIntersectionFalse(String r1x1, String r1x2, String r1y1, String r1y2,
                                                            String r2x1, String r2x2, String r2y1, String r2y2) {
        // Arrange
        Coordinates coordinates = new Coordinates(r1x1, r1x2, r1y1, r1y2, r2x1, r2x2, r2y1, r2y2);

        // Act
        RectangleResponse response = rectangleService.getRectangleResult(coordinates);

        // Assert
        assertFalse(response.isAdjacent());
        assertFalse(response.isContained());
        assertFalse(response.isIntersected());
        assertNull(response.getAdjacencyType());
        assertNull(response.getContaineeRectangle());
        assertNull(response.getContainerRectangle());
        assertNull(response.getIntersection());
    }

    @ParameterizedTest
    @MethodSource("testInvalidRectanglePoints")
    void testInvalidRectanglePoints(String r1x1, String r1x2, String r1y1, String r1y2,
                                    String r2x1, String r2x2, String r2y1, String r2y2,
                                    Class<? extends Exception> expectedClass, String expectedMessage) {
        // Arrange
        Coordinates coordinates = new Coordinates(r1x1, r1x2, r1y1, r1y2, r2x1, r2x2, r2y1, r2y2);

        // Act
        Exception invalidPointsException = assertThrows(expectedClass, () -> {
            rectangleService.getRectangleResult(coordinates);
        });

        String actualMessage = invalidPointsException.getMessage();

        // Assert
        assertTrue(actualMessage.contains(expectedMessage));
    }

    private static Stream<Arguments> testIntersectionParameters() {
        return Stream.of(
                Arguments.of("-1", "2", "0", "2", "-3", "1", "-1", "1",
                        buildRectangle(-1, 1, 0 ,1)),
                Arguments.of("-11", "-7", "-6", "-4", "-10", "-8", "-8", "-5",
                        buildRectangle(-10, -8, -6 ,-5))
        );
    }

    private static Stream<Arguments> testContainmentParameters() {
        return Stream.of(
                Arguments.of("3", "10", "-8", "-4", "4", "9", "-7", "-5",
                        buildRectangle(3, 10, -8, -4),
                        buildRectangle(4, 9, -7, -5)),
                Arguments.of("-14", "-9", "-3", "2", "-12", "-10", "-2", "1",
                        buildRectangle(-14, -9, -3, 2),
                        buildRectangle(-12, -10, -2, 1))
        );
    }

    private static Stream<Arguments> testAdjacencyParameters() {
        return Stream.of(
                Arguments.of("6", "11", "2", "6", "3", "6", "3", "5",
                        AdjacencyType.SUBLINE),
                Arguments.of("-13", "-8", "4", "6", "-8", "-5", "5", "9",
                        AdjacencyType.PARTIAL),
                Arguments.of("0", "4", "7", "10", "-3", "0", "7", "10",
                        AdjacencyType.PROPER)
        );
    }

    private static Stream<Arguments> testRectangleContainmentAdjacencyIntersectionFalse() {
        return Stream.of(
                Arguments.of("0", "4", "7", "10", "-13", "-8", "4", "6"),
                Arguments.of("6", "11", "2", "6", "-8", "-5", "5", "9"),
                Arguments.of("3", "6", "3", "5", "-3", "0", "7", "10"),
                Arguments.of("3", "10", "-8", "-4", "-12", "-10", "-2", "1"),
                Arguments.of("4", "9", "-7", "-5", "-14", "-9", "-3", "2"),
                Arguments.of("-3", "1", "-1", "1", "-11", "-7", "-6", "-4")
        );
    }

    private static Stream<Arguments> testInvalidRectanglePoints() {
        return Stream.of(
                Arguments.of("4", "0", "10", "7", "-8", "-13", "6", "4",
                        InvalidRectangleException.class, "Invalid rectangle points."),
                Arguments.of("11", "6", "6", "2", "-5", "-8", "9", "5",
                        InvalidRectangleException.class, "Invalid rectangle points."),
                Arguments.of("6", "3", "5", "3", "0", "-3", "10", "7",
                        InvalidRectangleException.class, "Invalid rectangle points."),
                Arguments.of("asd", "123", "asdf", "faa", "??", "-!!", "{{10}}", "cool",
                        NumberFormatException.class, "For input string")
        );
    }

    private static Rectangle buildRectangle(float x1, float x2, float y1, float y2) {
        Point bottomLeft = new Point(x1, y1);
        Point upperRight = new Point(x2, y2);

        return new Rectangle(bottomLeft, upperRight);
    }

}
