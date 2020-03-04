docker stop germes-admin-payara
docker container rm germes-admin-payara
docker build -t germes/admin-payara -f src/main/resources/docker/admin-payara.dockerfile .
docker run -it -p 9000:8080 -p 4849:4848 --name=germes-admin-payara --link germes_mysql:mysql germes/admin-payara