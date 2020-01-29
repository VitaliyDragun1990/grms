docker rmi germes/base
docker build -t germes/base -f germes-presentation-web/src/main/resources/docker/project-gradle.dockerfile .