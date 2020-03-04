docker rmi germes/base:payara
docker build --build-arg build_flag=-Ppayara -t germes/base:payara -f germes-client/src/main/resources/docker/project-gradle.dockerfile .