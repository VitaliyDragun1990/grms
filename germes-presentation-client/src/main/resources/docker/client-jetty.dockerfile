FROM jetty:9.4.18-jre11

ADD build/libs/client.war /var/lib/jetty/webapps/ROOT.war

# Run using docker console from germes-presentation-admin directory -> creates image with name germes/mysql
# docker build -t germes/admin-jetty -f src/main/resources/docker/admin-jetty.dockerfile . [--no-cache=true]

# Run container from created image and link it to mysql docker container
#docker run -it -p 8080:8080 --name=germes-admin-jetty --link germes_mysql:mysql germes/admin-jetty