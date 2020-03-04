FROM jboss/wildfly:18.0.1.Final

ADD build/libs/admin.war /opt/jboss/wildfly/standalone/deployments/

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]

RUN /opt/jboss/wildfly/bin/add-user.sh admin admin --silent

# Run using docker console from germes-presentation-admin directory -> creates image with name germes/mysql
# docker build -t germes/admin-wildfly -f src/main/resources/docker/admin-wildfly.dockerfile . [--no-cache=true]

# Run container from created image and link it to mysql docker container
#docker run -it -p 8080:8080 -p 9990:9990 --name=germes-admin-wildfly --link germes_mysql:mysql germes/admin-wildfly