
# Rectangles Application

This is a Spring application which calculates adjacency, containment and intersection between two rectangles.

## How to use

Construct two rectangles by sending the coordinates of bottom left and upper right points. Application will return
intersection points, adjacency type if any, and if there exists a containment between these two.

## Image example of adjacency, containment and intersection

![image info](.\src\test\resources\com.julian.rectangles.application\testCases.png)

## Docker

Build the image

`docker build --tag=rectangles-app:latest .`

Run the container

`docker run -p 8081:8081 rectangles-app:latest`

## Swagger

Check Swagger documentation locally in **localhost:8081/swagger-doc.html**