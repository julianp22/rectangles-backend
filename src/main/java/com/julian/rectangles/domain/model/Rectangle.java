package com.julian.rectangles.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@Getter
public class Rectangle {

    private Point bottomLeft;
    private Point upperRight;

    /**
     * Validates adjacency between two rectangles.
     *
     * @param anotherRectangle the other rectangle
     * @return  {@code true} if this rectangle is adjacent to the other rectangle;
     *          {@code false} otherwise.
     */
    public boolean isAdjacentTo(Rectangle anotherRectangle) {
        return isYAdjacent(anotherRectangle) || isXAdjacent(anotherRectangle);
    }

    private boolean isXAdjacent(Rectangle anotherRectangle) {
        return this.bottomLeft.getY() == anotherRectangle.upperRight.getY()
                || this.upperRight.getY() == anotherRectangle.bottomLeft.getY();
    }

    private boolean isYAdjacent(Rectangle anotherRectangle) {
        return this.bottomLeft.getX() == anotherRectangle.upperRight.getX()
                || this.upperRight.getX() == anotherRectangle.bottomLeft.getX();
    }

    /**
     * Validates proper adjacency between two rectangles. Proper adjacency exists if the shared side of this rectangle
     * perfectly aligns with the corresponding side of the other rectangle with no overlap or gap between them.
     *
     * @param anotherRectangle the other rectangle
     * @return  {@code true} if this rectangle is fully adjacent to the other rectangle;
     *          {@code false} otherwise.
     */
    public boolean hasProperAdjacencyWith(Rectangle anotherRectangle) {
        return isYFullyAdjacent(anotherRectangle) || isXFullyAdjacent(anotherRectangle);
    }

    private boolean isYFullyAdjacent(Rectangle anotherRectangle) {
        return this.upperRight.getY() == anotherRectangle.getUpperRight().getY()
                && this.bottomLeft.getY() == anotherRectangle.getBottomLeft().getY();
    }

    private boolean isXFullyAdjacent(Rectangle anotherRectangle) {
        return this.upperRight.getX() == anotherRectangle.getUpperRight().getX()
                && this.bottomLeft.getX() == anotherRectangle.getBottomLeft().getX();
    }

    /**
     * Validates subline adjacency between two rectangles. Partial adjacency exists if some line segment on a
     * side of this rectangle exists as a set of points on some side of the other rectangle.
     *
     * @param anotherRectangle the other rectangle
     * @return  {@code true} if this rectangle is partially adjacent to the other rectangle;
     *          {@code false} otherwise.
     */
    public boolean hasPartialAdjacencyWith(Rectangle anotherRectangle) {
        boolean partialAdjacent = false;
        if (isYAdjacent(anotherRectangle)) {
            partialAdjacent = isYPartialAdjacent(anotherRectangle);
        } else if (isXAdjacent(anotherRectangle)) {
            partialAdjacent = isXPartialAdjacent(anotherRectangle);
        }
        return partialAdjacent;
    }


    private boolean isYPartialAdjacent(Rectangle anotherRectangle) {
        return (this.upperRight.getY() > anotherRectangle.getUpperRight().getY()
                && this.bottomLeft.getY() < anotherRectangle.getUpperRight().getY()
                && this.bottomLeft.getY() > anotherRectangle.getBottomLeft().getY())
                || (this.upperRight.getY() > anotherRectangle.getBottomLeft().getY()
                && this.bottomLeft.getY() < anotherRectangle.getBottomLeft().getY()
                && this.upperRight.getY() < anotherRectangle.getUpperRight().getY());
    }

    private boolean isXPartialAdjacent(Rectangle anotherRectangle) {
        return (this.upperRight.getX() > anotherRectangle.getUpperRight().getX()
                && this.bottomLeft.getX() < anotherRectangle.getUpperRight().getX()
                && this.bottomLeft.getX() > anotherRectangle.getBottomLeft().getX())
                || (this.upperRight.getX() > anotherRectangle.getBottomLeft().getX()
                && this.bottomLeft.getX() < anotherRectangle.getBottomLeft().getX()
                && this.upperRight.getX() < anotherRectangle.getUpperRight().getX());
    }

    /**
     * Validates subline adjacency between two rectangles. Subline adjacency exists when one side of this rectangle
     * is a line that exists as a set of points wholly contained on some other side of rectangle B.
     *
     * @param anotherRectangle the other rectangle
     * @return  {@code true} if this rectangle is subline adjacent to the other rectangle;
     *          {@code false} otherwise.
     */
    public boolean hasSublineAdjacencyWith(Rectangle anotherRectangle) {
        return isYSublineAdjacent(anotherRectangle) || isXSublineAdjacent(anotherRectangle);
    }

    private boolean isYSublineAdjacent(Rectangle anotherRectangle) {
        return this.upperRight.getY() > anotherRectangle.getUpperRight().getY()
                && this.bottomLeft.getY() < anotherRectangle.getBottomLeft().getY();
    }

    private boolean isXSublineAdjacent(Rectangle anotherRectangle) {
        return this.upperRight.getX() > anotherRectangle.getUpperRight().getX()
                && this.bottomLeft.getX() < anotherRectangle.getBottomLeft().getX();
    }

    /**
     * Validates the containment between two rectangles
     *
     * @param anotherRectangle the other rectangle
     * @return  {@code true} if this rectangle contains the other rectangle;
     *          {@code false} otherwise.
     */
    public boolean containsRectangle(Rectangle anotherRectangle) {
        return this.bottomLeft.getY() < anotherRectangle.getBottomLeft().getY()
                && this.bottomLeft.getX() < anotherRectangle.getBottomLeft().getX()
                && this.upperRight.getY() > anotherRectangle.getUpperRight().getY()
                && this.upperRight.getX() > anotherRectangle.getUpperRight().getX();
    }

    /**
     * Validates the intersection between two rectangles
     *
     * @param anotherRectangle the other rectangle
     * @return  Optional of {@code Rectangle} if there's intersection;
     *          Optional of {@code Empty} otherwise.
     */
    public Optional<Rectangle> getIntersection(Rectangle anotherRectangle) {
        if (yAxisNotIntersected(anotherRectangle) || xAxisNotIntersected(anotherRectangle)) {
            return Optional.empty();
        }

        float x1 = Math.max(this.bottomLeft.getX(), anotherRectangle.bottomLeft.getX());
        float y1 = Math.max(this.bottomLeft.getY(), anotherRectangle.bottomLeft.getY());
        float x2 = Math.min(this.upperRight.getX(), anotherRectangle.upperRight.getX());
        float y2 = Math.min(this.upperRight.getY(), anotherRectangle.upperRight.getY());

        return Optional.of(new Rectangle(new Point(x1, y1), new Point(x2, y2)));
    }

    private boolean yAxisNotIntersected(Rectangle anotherRectangle) {
        return this.upperRight.getY() <= anotherRectangle.bottomLeft.getY()
                || this.bottomLeft.getY() >= anotherRectangle.upperRight.getY();
    }

    private boolean xAxisNotIntersected(Rectangle anotherRectangle) {
        return this.upperRight.getX() <= anotherRectangle.bottomLeft.getX()
                || this.bottomLeft.getX() >= anotherRectangle.upperRight.getX();
    }

    /**
     * Validates correct positioning of bottom left and upper right points
     *
     * @param bottomLeft bottom left point
     * @param upperRight upper right point
     * @return  {@code true} if bottom and upper points are in correct position order;
     *          {@code false} otherwise.
     */
    public static boolean isRectangleValid(Point bottomLeft, Point upperRight) {
        return bottomLeft.getX() < upperRight.getX() && bottomLeft.getY() < upperRight.getY();
    }

    @Override
    public String toString() {
        return "Rectangle: (bottomLeft: x=" + this.bottomLeft.getX() + ", y=" + this.bottomLeft.getY()
                + ", upperRight: x=" + this.upperRight.getX() + ", y=" + this.upperRight.getY() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rectangle rectangle = (Rectangle) o;
        return Objects.equals(this.bottomLeft, rectangle.bottomLeft) &&
                Objects.equals(upperRight, rectangle.upperRight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bottomLeft, upperRight);
    }

}
