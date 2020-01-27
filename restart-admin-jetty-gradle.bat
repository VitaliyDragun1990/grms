docker stop germes-admin-jetty-gradle
docker container rm germes-admin-jetty-gradle
docker build -t germes/admin-jetty-gradle -f germes-presentation-admin/src/main/resources/docker/admin-jetty-gradle.dockerfile .
docker run -it -p 9000:8080 --name=germes-admin-jetty-gradle --link germes_mysql:mysql germes/admin-jetty-gradle