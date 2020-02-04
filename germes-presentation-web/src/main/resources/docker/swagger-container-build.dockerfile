FROM germes/base AS germes

FROM swaggerapi/swagger-ui

COPY --from=germes /opt/swagger.json /opt/swagger.json