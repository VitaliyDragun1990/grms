docker stop germes-client-tomcat-gradle
docker container rm germes-client-tomcat-gradle
docker build -t germes/client-tomcat-gradle -f germes-presentation-client/src/main/resources/docker/client-tomcat-gradle.dockerfile .
docker run -it -p 8081:8080 --name=germes-client-tomcat-gradle --link germes_mysql:mysql germes/client-tomcat-gradle