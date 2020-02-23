FROM germes/base AS base

FROM openjdk:11-jre-stretch

RUN apt-get update && apt-get -y install curl

ARG jar_path

COPY --from=base $jar_path /opt/