docker rmi germes/base:gradle
docker build -t germes/base:gradle -f germes-presentation-web/src/main/resources/docker/project-gradle.dockerfile .