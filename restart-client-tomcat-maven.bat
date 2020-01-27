docker stop germes-client-tomcat-maven
docker container rm germes-client-tomcat-maven
docker build -t germes/client-tomcat-maven -f germes-presentation-client/src/main/resources/docker/client-tomcat-maven.dockerfile .
docker run -it -p 8080:8080 --name=germes-client-tomcat-maven --link germes_mysql:mysql germes/client-tomcat-maven