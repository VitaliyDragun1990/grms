# Image with jdk13 jvm to run jar files
FROM openjdk:13-jdk-alpine

RUN apk update && apk add curl

ARG jar_path

COPY $jar_path /opt/