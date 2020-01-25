docker stop germes-client-tomcat
docker container rm germes-client-tomcat
docker build -t germes/client-tomcat -f src/main/resources/docker/client-tomcat.dockerfile .
docker run -it -p 8081:8080 --name=germes-client-tomcat --link germes_mysql:mysql germes/client-tomcat