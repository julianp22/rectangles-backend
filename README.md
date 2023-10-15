
# Rectangles Application

This is a Spring application which calculates adjacency, containment and intersection between two rectangles.

## How to use

Construct two rectangles by sending the coordinates of bottom left and upper right points. Application will return
intersection points, adjacency type if any, and if there exists a containment between these two.

## Image example of adjacency, containment and intersection

![testCases](https://github.com/julianp22/rectangles-backend/assets/28449098/f11b1c10-9f01-46f9-a458-324f6587a5d4)

## Docker

Build the image

`docker build --tag=rectangles-app:latest .`

Run the container

`docker run -p 8081:8081 rectangles-app:latest`

## Swagger

Check Swagger documentation locally in **localhost:8081/swagger-doc.html**
