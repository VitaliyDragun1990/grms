docker stop germes-client-jetty-maven
docker container rm germes-client-jetty-maven
docker build -t germes/client-jetty-maven -f germes-presentation-client/src/main/resources/docker/client-jetty-maven.dockerfile .
docker run -it -p 8080:8080 --name=germes-client-jetty-maven --link germes_mysql:mysql germes/client-jetty-maven