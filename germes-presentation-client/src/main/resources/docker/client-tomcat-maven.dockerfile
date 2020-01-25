FROM maven:3.6.3-jdk-11-slim AS maven3

COPY . /opt/maven/

WORKDIR /opt/maven

RUN mvn clean package -Dmaven.test.skip=true

FROM tomcat:9.0-jdk11-openjdk-slim

COPY --from=maven3 /opt/maven/germes-presentation-client/target/client.war /usr/local/tomcat/webapps/ROOT.war

# Run using docker console from germes-presentation-admin directory -> creates image with name germes/mysql
# docker build -t germes/client-tomcat -f src/main/resources/docker/client-tomcat-maven.dockerfile . [--no-cache=true]

# Run container from created image and link it to mysql docker container
#docker run -it -p 8081:8080 --name=germes-client-tomcat --link germes_mysql:mysql germes/client-tomcat-maven

# Run container from created image in debag mode and link it to mysql docker container
#docker run -it -p 8081:8080 --name=germes-client-tomcat --link germes_mysql:mysql \
# -e JPDA_ADDRESS=8000 -e JPDA_TRANSPORT_TYPE=dt_socket -p 8001:8000 \
# germes/client-tomcat-maven \
# /usr/local/tomcat/bin/catalina.sh jpda run