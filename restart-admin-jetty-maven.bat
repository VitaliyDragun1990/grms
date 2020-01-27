docker stop germes-admin-jetty-maven
docker container rm germes-admin-jetty-maven
docker build -t germes/admin-jetty-maven -f germes-presentation-admin/src/main/resources/docker/admin-jetty-maven.dockerfile .
docker run -it -p 9000:8080 --name=germes-admin-jetty-maven --link germes_mysql:mysql germes/admin-jetty-maven