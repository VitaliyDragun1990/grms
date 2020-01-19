# Base image from which current one will be created
FROM mysql:5.7
# Addinitonal info about author, organization, enc
LABEL Author="Vitaliy Dragun", Version=0.1
# Define environment variables for image to use
ENV MYSQL_ROOT_PASSWORD=root \
    MYSQL_USER=germes \
    MYSQL_PASSWORD=germes \
    MYSQL_DATABASE=germes

# Run using docker console from this dirrectory
# docker build -t germes/mysql -f mysql.dockerfile . [--no-cache=true]

# Run container from created image
# docker run --name germes_db --publish 3307:3306 -e MYSQL_ROOT_PASSWORD=19900225 -d germes/mysql