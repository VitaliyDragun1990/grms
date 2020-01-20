# Base image from which current one will be created
FROM mysql:5.7
# Addinitonal info about author, organization, enc
LABEL Author="Vitaliy Dragun", Version=0.1
# Define environment variables for image to use
ENV MYSQL_ROOT_PASSWORD=root \
    MYSQL_USER=germes \
    MYSQL_PASSWORD=germes \
    MYSQL_DATABASE=germes

ADD germes-presentation-admin/src/main/resources/docker/grant.sql /docker-entrypoint-initdb.d/

# Run using docker console from this dirrectory -> creates image with name germes/mysql
# docker build -t germes/mysql -f mysql.dockerfile . [--no-cache=true]

# Run container from created image
# docker run --name germes_db --publish 3307:3306 -e MYSQL_ROOT_PASSWORD=19900225 --memory=512M \
# --volume /var/project_data/germes/database/mysql:/var/lib/mysql --cpus=1 -d germes/mysql

# Run container and link it to jetty container
#docker run --name germes_mysql -e MYSQL_ROOT_PASSWORD=19900225 --memory=512M p 3307:3306 \
# --volume /var/project_data/germes/database/mysql:/var/lib/mysql --cpus=1 -d germes/mysql
docker run -it -v /projects/germes/germes-presentation-admin/build/libs:/var/lib/jetty/webapps -p 8080:8080 \
 --name=germes_admin_jetty --link germes_mysql:mysql jetty:9.4.18-jre11