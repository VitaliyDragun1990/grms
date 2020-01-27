docker stop germes-admin-wildfly
docker container rm germes-admin-wildfly
docker build -t germes/admin-wildfly -f src/main/resources/docker/admin-wildfly.dockerfile .
docker run -it -p 9000:8080 -p 9991:9990 --name=germes-admin-wildfly --link germes_mysql:mysql germes/admin-wildfly