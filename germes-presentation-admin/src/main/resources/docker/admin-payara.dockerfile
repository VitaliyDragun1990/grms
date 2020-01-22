FROM ivonet/payara:5.194

ADD build/libs/admin.war $DEPLOY_DIR

# Run using docker console from germes-presentation-admin directory -> creates image with name germes/mysql
# docker build -t germes/admin-payara -f src/main/resources/docker/admin-payara.dockerfile . [--no-cache=true]

# Run container from created image and link it to mysql docker container
#docker run -it -p 8080:8080 -p 4848:4848 --name=germes-admin-payara --link germes_mysql:mysql germes/admin-payara