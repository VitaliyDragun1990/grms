# From gradle image
FROM gradle:6.0-jdk11 AS gradle6

# to customise build for Payara/Wildfly
ARG build_flag

USER root

# Copy gradle.build files from all modules and install all necessary dependencies
COPY build.gradle /home/gradle/build.gradle
COPY gradle.properties /home/gradle/gradle.properties
COPY settings.gradle /home/gradle/settings.gradle
COPY germes-common/build.gradle /home/gradle/germes-common/build.gradle
COPY germes-common-rest/build.gradle /home/gradle/germes-common-rest/build.gradle
COPY germes-common-monitoring/build.gradle /home/gradle/germes-common-monitoring/build.gradle
COPY germes-common-persistence/build.gradle /home/gradle/germes-common-persistence/build.gradle
COPY germes-geography-service/build.gradle /home/gradle/germes-geography-service/build.gradle
COPY germes-geography-service-client/build.gradle /home/gradle/germes-geography-service-client/build.gradle
COPY germes-presentation-web/build.gradle /home/gradle/germes-presentation-web/build.gradle
COPY germes-presentation-admin/build.gradle /home/gradle/germes-presentation-admin/build.gradle
COPY germes-ticket-service/build.gradle /home/gradle/germes-ticket-service/build.gradle
COPY germes-trip-service/build.gradle /home/gradle/germes-trip-service/build.gradle
COPY germes-user-service/build.gradle /home/gradle/germes-user-service/build.gradle
COPY germes-user-service-client/build.gradle /home/gradle/germes-user-service-client/build.gradle

WORKDIR /home/gradle

# Skip building and testing angular project, because it was already build above in node13-based container
RUN gradle -PskipAngular downloadDependencies

COPY . /home/gradle/

# Build applicaitons
RUN gradle $build_flag -PskipAngular clean build && \
    cp /home/gradle/germes-geography-service/build/libs/geography-service.war /opt && \
    cp /home/gradle/germes-presentation-admin/build/libs/admin.war /opt && \
    cp /home/gradle/germes-user-service/build/libs/user-service.jar /opt && \
    cp /home/gradle/germes-user-service/build/libs/ticket-service.jar /opt && \
    rm -rf /home/gradle/germes*

# From project root directory
# docker build [--build-arg build_flag=-Ppayara] -t germes/base[:payara] \
# -f germes-presentation-web/src/main/resources/docker/project-gradle.dockerfile .