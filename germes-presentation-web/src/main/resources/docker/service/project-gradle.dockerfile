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
COPY germes-persistence-base/build.gradle /home/gradle/germes-persistence-base/build.gradle
COPY germes-geography-service/build.gradle /home/gradle/germes-geography-service/build.gradle
COPY germes-presentation-web/build.gradle /home/gradle/germes-presentation-web/build.gradle
#COPY germes-presentation-admin/build.gradle /home/gradle/germes-presentation-admin/build.gradle
#COPY germes-presentation-rest/build.gradle /home/gradle/germes-presentation-rest/build.gradle
#COPY germes-application-model/build.gradle /home/gradle/germes-application-model/build.gradle
#COPY germes-application-service/build.gradle /home/gradle/germes-application-service/build.gradle
#COPY germes-persistence/build.gradle /home/gradle/germes-persistence/build.gradle

WORKDIR /home/gradle

# Skip building and testing angular project, because it was already build above in node13-based container
RUN gradle -PskipAngular downloadDependencies

COPY . /home/gradle/

# Build applicaitons
RUN gradle $build_flag -PskipAngular clean build && \
    cp /home/gradle/germes-geography-service/build/libs/geography-service.war /opt && \
#    cp /home/gradle/germes-presentation-admin/build/libs/admin.war /opt && \
    rm -rf /home/gradle/germes*

# From project root directory
# docker build [--build-arg build_flag=-Ppayara] -t germes/base[:payara] \
# -f germes-presentation-web/src/main/resources/docker/project-gradle.dockerfile .