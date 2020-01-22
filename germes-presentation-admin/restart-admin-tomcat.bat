docker stop germes-admin-tomcat
docker container rm germes-admin-tomcat
docker build -t germes/admin-jetty -f src/main/resources/docker/admin-tomcat.dockerfile .
docker run -it -p 8080:8080 --name=germes-admin-tomcat --link germes_mysql:mysql germes/admin-tomcat