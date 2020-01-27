docker stop germes-client-payara
docker container rm germes-client-payara
docker build -t germes/client-payara -f src/main/resources/docker/client-payara.dockerfile .
docker run -it -p 8080:8080 -p 4848:4848 --name=germes-client-payara --link germes_mysql:mysql germes/client-payara