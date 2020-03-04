# From node image
FROM node:13-slim AS node13

RUN apt-get update && apt-get install -y bzip2

# Copy package.json file and install all angular dependencies for germes-presentation-web module
COPY germes-presentation-web/client/package.json /opt/client/package.json
WORKDIR /opt/client/

RUN yarn install

COPY germes-presentation-web/client/ /opt/client

RUN node_modules/.bin/ng build --prod

# From gradle image
FROM gradle:6.0-jdk11 AS gradle6

# to customise build for Payara/Wildfly
ARG build_flag

USER root

# Copy gradle.build files from all modules and install all necessary dependencies
COPY build.gradle /home/gradle/build.gradle
COPY gradle.properties /home/gradle/gradle.properties
COPY settings.gradle /home/gradle/settings.gradle
COPY germes-presentation-web/build.gradle /home/gradle/germes-presentation-web/build.gradle
COPY germes-presentation-admin/build.gradle /home/gradle/germes-presentation-admin/build.gradle
COPY germes-presentation-rest/build.gradle /home/gradle/germes-presentation-rest/build.gradle
COPY germes-application-model/build.gradle /home/gradle/germes-application-model/build.gradle
COPY germes-application-service/build.gradle /home/gradle/germes-application-service/build.gradle
COPY germes-persistence/build.gradle /home/gradle/germes-persistence/build.gradle

WORKDIR /home/gradle

# Skip building and testing angular project, because it was already build above in node13-based container
RUN gradle -PskipAngular downloadDependencies

# Copy all project source code files and build client and admin applications
COPY . /home/gradle/

# Copy already build angular propject from node13 layer to maven3 layer
COPY --from=node13 /opt/client/dist/ /home/gradle/germes-presentation-web/client/dist

# Build client and admin applicaitons
RUN gradle $build_flag -PskipAngular clean build && \
    cp /home/gradle/germes-presentation-web/build/libs/client.war /opt && \
    cp /home/gradle/germes-presentation-admin/build/libs/admin.war /opt && \
    rm -rf /home/gradle/germes*

# From project root directory
# docker build [--build-arg build_flag=-Ppayara] -t germes/base[:payara] \
# -f germes-presentation-web/src/main/resources/docker/project-gradle.dockerfile .