docker stop germes-admin-jetty
docker container rm germes-admin-jetty
docker build -t germes/admin-jetty -f src/main/resources/docker/admin-jetty.dockerfile .
docker run -it -p 9000:8080 --name=germes-admin-jetty --link germes_mysql:mysql germes/admin-jetty