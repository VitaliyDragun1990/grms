# Base image from which current one will be created
FROM mysql:8.0.19
# Addinitonal info about author, organization, enc
LABEL Author="Vitaliy Dragun", Version=0.1
# Define environment variables for image to use
ENV MYSQL_ROOT_PASSWORD=19900225

ADD src/main/resources/docker/grant.sql /docker-entrypoint-initdb.d/

# Run using docker console from germes-presentation-admin directory -> creates image with name germes/mysql
# docker build -t germes/mysql -f src/main/resources/docker/mysql.dockerfile . [--no-cache=true]

# Create volume to keep database data
# docker volume create --name germes-mysql-data

# Run container from created image
#docker run --name germes_mysql -e MYSQL_ROOT_PASSWORD=19900225 --memory=512M --cpus=1 -p 3307:3306 \
#--volume germes-mysql-data:/var/lib/mysql -d germes/mysql --collation-server=utf8mb4_unicode_ci --character-set-server=utf8mb4