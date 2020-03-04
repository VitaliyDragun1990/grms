# From node image
FROM node:13-slim AS node13

RUN apt-get update && apt-get install -y bzip2

# Copy package.json file and install all angular dependencies for germes-presentation-web module
COPY germes-presentation-web/client/package.json /opt/client/package.json
WORKDIR /opt/client/

RUN yarn install

COPY germes-presentation-web/client/ /opt/client

RUN node_modules/.bin/ng build --prod

# From maven image
FROM maven:3.6.3-jdk-11-slim AS maven3

# to customise build for Payara/Wildfly
ARG build_flag

# Copy already build angular propject from node13 layer to maven3 layer
COPY --from=node13 /opt/client/dist/ /opt/maven/germes-presentation-web/client/dist

# Copy pom.xml files from all modules and install all necessary dependencies
COPY pom.xml /opt/maven/pom.xml
COPY germes-presentation-admin/pom.xml /opt/maven/germes-presentation-admin/pom.xml
COPY germes-presentation-web/pom.xml /opt/maven/germes-presentation-web/pom.xml
COPY germes-presentation-rest/pom.xml /opt/maven/germes-presentation-rest/pom.xml
COPY germes-application-model/pom.xml /opt/maven/germes-application-model/pom.xml
COPY germes-application-service/pom.xml /opt/maven/germes-application-service/pom.xml
COPY germes-persistence/pom.xml /opt/maven/germes-persistence/pom.xml

WORKDIR /opt/maven

RUN mkdir /opt/maven/germes-presentation-web/empty
RUN mvn verify -Dmaven.exec.skip=true -Dangular.dist.folder=empty

# Copy all project source code files and build client and admin applications
COPY . /opt/maven/

# Build client and admin applicaitons
RUN mvn clean package -Dmaven.exec.skip=true $build_flag && \
    cp /opt/maven/germes-presentation-web/target/client.war /opt && \
    cp /opt/maven/germes-presentation-admin/target/admin.war /opt && \
    cp /opt/maven/germes-presentation-web/target/generated/swagger-ui/swagger.json /opt && \
    rm -rf /opt/maven

# From project root directory
# docker build [--build-arg build_flag=-Ppayara] -t germes/base[:payara] \
# -f germes-presentation-web/src/main/resources/docker/project-maven.dockerfile .