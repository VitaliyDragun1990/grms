docker rmi germes/base
docker build -t germes/base -f germes-client/src/main/resources/docker/service/project-maven.dockerfile .