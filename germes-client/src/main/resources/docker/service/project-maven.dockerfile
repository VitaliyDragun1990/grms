# From maven image
FROM maven:3.6.3-jdk-13 AS maven3

# to customise build for Payara/Wildfly
ARG build_flag

# Copy pom.xml files from all modules and install all necessary dependencies
COPY pom.xml /opt/maven/pom.xml
COPY germes-common/pom.xml /opt/maven/germes-common/pom.xml
COPY germes-common-monitoring/pom.xml /opt/maven/germes-common-monitoring/pom.xml
COPY germes-common-persistence/pom.xml /opt/maven/germes-common-persistence/pom.xml
COPY germes-common-rest/pom.xml /opt/maven/germes-common-rest/pom.xml
COPY germes-geography-service/pom.xml /opt/maven/germes-geography-service/pom.xml
COPY germes-geography-service-client/pom.xml /opt/maven/germes-geography-service-client/pom.xml
COPY germes-user-service/pom.xml /opt/maven/germes-user-service/pom.xml
COPY germes-user-service-client/pom.xml /opt/maven/germes-user-service-client/pom.xml
COPY germes-trip-service/pom.xml /opt/maven/germes-trip-service/pom.xml
COPY germes-ticket-service/pom.xml /opt/maven/germes-ticket-service/pom.xml
COPY germes-presentation-web/pom.xml /opt/maven/germes-presentation-web/pom.xml
COPY germes-presentation-admin/pom.xml /opt/maven/germes-presentation-admin/pom.xml

WORKDIR /opt/maven

RUN mkdir /opt/maven/germes-presentation-web/empty
RUN mvn verify -Dmaven.exec.skip=true -Dangular.dist.folder=empty

# Copy all project source code files and build client and admin applications
COPY . /opt/maven/

# Build client and admin applicaitons
RUN mvn clean package -Dmaven.exec.skip=true $build_flag && \
    cp /opt/maven/germes-geography-service/target/geography-service.war /opt && \
    cp /opt/maven/germes-user-service/target/user-service.jar /opt && \
    cp /opt/maven/germes-user-service/target/ticket-service.jar /opt && \
    cp /opt/maven/germes-presentation-admin/target/admin.war /opt && \
    rm -rf /opt/maven

# From project root directory
# docker build [--build-arg build_flag=-Ppayara] -t germes/base[:payara] \
# -f germes-presentation-web/src/main/resources/docker/project-maven.dockerfile .