FROM germes/base AS base

FROM openjdk:13-jdk-alpine

RUN apk update && apk add curl

ARG jar_path

COPY --from=base $jar_path /opt/