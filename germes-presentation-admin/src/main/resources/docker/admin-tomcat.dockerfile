FROM tomcat:9.0-jdk11-openjdk-slim

ADD build/libs/admin.war /usr/local/tomcat/webapps/

# Run using docker console from germes-presentation-admin directory -> creates image with name germes/mysql
# docker build -t germes/admin-tomcat -f src/main/resources/docker/admin-tomcat.dockerfile . [--no-cache=true]

# Run container from created image and link it to mysql docker container
#docker run -it -p 8080:8080 --name=germes_admin_tomcat --link germes_mysql:mysql germes/admin-tomcat

# Run container from created image in debag mode and link it to mysql docker container
#docker run -it -p 8080:8080 --name=germes_admin_tomcat --link germes_mysql:mysql \
# -e JPDA_ADDRESS=8000 -e JPDA_TRANSPORT_TYPE=dt_socket -p 8000:8000 \
# germes/admin-tomcat \
# /usr/local/tomcat/bin/catalina.sh jpda run