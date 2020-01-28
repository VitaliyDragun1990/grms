FROM maven:3.6.3-jdk-11-slim AS maven3

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

RUN mvn clean package

FROM tomcat:9.0-jdk11-openjdk-slim

RUN rm -rf /usr/local/tomcat/webapps/ROOT

COPY --from=maven3 /opt/maven/germes-presentation-client/target/client.war /usr/local/tomcat/webapps/ROOT.war