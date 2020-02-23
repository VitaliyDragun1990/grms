# Image with jdk11 jvm to run jar files
FROM openjdk:11-jre-stretch

RUN apt-get update && apt-get -y install curl

ARG jar_path

COPY $jar_path /opt/