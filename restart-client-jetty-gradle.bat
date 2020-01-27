docker stop germes-client-jetty-gradle
docker container rm germes-client-jetty-gradle
docker build -t germes/client-jetty-gradle -f germes-presentation-client/src/main/resources/docker/client-jetty-gradle.dockerfile .
docker run -it -p 8080:8080 --name=germes-client-jetty-gradle --link germes_mysql:mysql germes/client-jetty-gradle