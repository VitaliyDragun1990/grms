FROM maven:3.6.3-jdk-11-slim AS maven3

# to customise build for Payara/Wildfly
ARG build_flag

# Update package info, install GnuPG, install NodeJS(10) with NPM 5.6
RUN apt-get update && apt-get install -y gnupg && \
 curl -sL https://deb.nodesource.com/setup_10.x | bash - && apt-get install -y nodejs && \
 apt-get install -y build-essential

# Copy package.json file and install all angular dependencies for germes-presentation-web module
COPY germes-presentation-web/client/package.json /opt/maven/germes-presentation-web/client/package.json
WORKDIR /opt/maven/germes-presentation-web/client
RUN npm install

# Copy pom.xml files from all modules and install all necessary dependencies
COPY pom.xml /opt/maven/pom.xml
COPY germes-presentation-admin/pom.xml /opt/maven/germes-presentation-admin/pom.xml
COPY germes-presentation-web/pom.xml /opt/maven/germes-presentation-web/pom.xml
COPY germes-presentation-rest/pom.xml /opt/maven/germes-presentation-rest/pom.xml
COPY germes-application-model/pom.xml /opt/maven/germes-application-model/pom.xml
COPY germes-application-service/pom.xml /opt/maven/germes-application-service/pom.xml
COPY germes-persistence/pom.xml /opt/maven/germes-persistence/pom.xml
RUN mkdir /opt/maven/germes-presentation-web/empty
WORKDIR /opt/maven
RUN mvn verify -e -Dmaven.exec.skip=true -Dangular.dist.folder=empty

# Copy all project source code files and build client and admin applications
COPY . /opt/maven/
RUN mvn clean package $build_flag && cp /opt/maven/germes-presentation-web/target/client.war /opt && \
    cp /opt/maven/germes-presentation-admin/target/admin.war /opt && \
    rm -rf /opt/maven

# From project root directory
# docker build [--build-arg build_flag=-Ppayara] -t germes/base[:payara] \
# -f germes-presentation-web/src/main/resources/docker/project-maven.dockerfile .