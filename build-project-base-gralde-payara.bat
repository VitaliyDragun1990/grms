docker rmi germes/base:payara
docker build --build-arg build_flag=-Ppayara -t germes/base:payara -f germes-presentation-web/src/main/resources/docker/project-gradle.dockerfile .