# From node image
FROM node:13-slim AS node13

RUN apt-get update && apt-get install -y bzip2

# Copy package.json file and install all angular dependencies for germes-presentation-web module
COPY germes-presentation-web/client/package.json /opt/client/package.json
WORKDIR /opt/client/

RUN yarn install

COPY germes-presentation-web/client/ /opt/client

RUN node_modules/.bin/ng build --prod

# Apache HTTP Server
FROM httpd:2

# Copy built angular project files to Apache server
COPY --from=node13 /opt/client/dist/ /usr/local/apache2/htdocs/
