docker stop germes-client-jetty
docker container rm germes-client-jetty
docker build -t germes/client-jetty -f src/main/resources/docker/client-jetty.dockerfile .
docker run -it -p 8080:8080 --name=germes-client-jetty --link germes_mysql:mysql germes/client-jetty