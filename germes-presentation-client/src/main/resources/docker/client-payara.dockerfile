FROM ivonet/payara:5.194

ADD build/libs/client.war $DEPLOY_DIR

# Run using docker console from germes-presentation-admin directory -> creates image with name germes/mysql
# docker build -t germes/client-payara -f src/main/resources/docker/client-payara.dockerfile . [--no-cache=true]

# Run container from created image and link it to mysql docker container
#docker run -it -p 8081:8080 -p 4849:4848 --name=germes-client-payara --link germes_mysql:mysql germes/client-payara