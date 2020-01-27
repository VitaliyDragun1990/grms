FROM gradle:6.0-jdk11 AS gradle6

# to customise build for Payara/Wildfly
ARG build_flag

USER root

COPY build.gradle /home/gradle/project/build.gradle
COPY gradle.properties /home/gradle/project/gradle.properties
COPY settings.gradle /home/gradle/project/settings.gradle
COPY germes-presentation-client/build.gradle /home/gradle/project/germes-presentation-client/build.gradle
COPY germes-presentation-admin/build.gradle /home/gradle/project/germes-presentation-admin/build.gradle
COPY germes-presentation-rest/build.gradle /home/gradle/project/germes-presentation-rest/build.gradle
COPY germes-application-model/build.gradle /home/gradle/project/germes-application-model/build.gradle
COPY germes-application-service/build.gradle /home/gradle/project/germes-application-service/build.gradle
COPY germes-persistence/build.gradle /home/gradle/project/germes-persistence/build.gradle

WORKDIR /home/gradle/project

RUN gradle test

COPY . /home/gradle/project/

RUN gradle clean assemble $build_flag && cp /home/gradle/project/germes-presentation-client/build/libs/client.war /home/gradle && \
    cp /home/gradle/project/germes-presentation-admin/build/libs/admin.war /home/gradle && \
    rm -rf /home/gradle/project

# From project root directory
# docker build [--build-arg build_flag=-Ppayara] -t germes/base[:payara] \
# -f germes-presentation-client/src/main/resources/docker/project-gradle.dockerfile .