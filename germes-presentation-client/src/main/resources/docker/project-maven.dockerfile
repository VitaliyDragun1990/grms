FROM maven:3.6.3-jdk-11-slim AS maven3

# to customise build for Payara/Wildfly
ARG build_flag

COPY pom.xml /opt/maven/pom.xml
COPY germes-presentation-admin/pom.xml /opt/maven/germes-presentation-admin/pom.xml
COPY germes-presentation-client/pom.xml /opt/maven/germes-presentation-client/pom.xml
COPY germes-presentation-rest/pom.xml /opt/maven/germes-presentation-rest/pom.xml
COPY germes-application-model/pom.xml /opt/maven/germes-application-model/pom.xml
COPY germes-application-service/pom.xml /opt/maven/germes-application-service/pom.xml
COPY germes-persistence/pom.xml /opt/maven/germes-persistence/pom.xml

WORKDIR /opt/maven

RUN mvn verify

COPY . /opt/maven/

RUN mvn clean package $build_flag && cp /opt/maven/germes-presentation-client/target/client.war /opt && \
    cp /opt/maven/germes-presentation-admin/target/admin.war /opt && \
    rm -rf /opt/maven

# From project root directory
# docker build [--build-arg build_flag=-Ppayara] -t germes/base[:payara] \
# -f germes-presentation-client/src/main/resources/docker/project-maven.dockerfile .