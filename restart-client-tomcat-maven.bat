docker stop germes-client-tomcat
docker container rm germes-client-tomcat
docker build -t germes/client-tomcat -f germes-presentation-client/src/main/resources/docker/client-tomcat-maven.dockerfile .
docker run -it -p 8081:8080 --name=germes-client-tomcat --link germes_mysql:mysql germes/client-tomcat