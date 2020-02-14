FROM graylog/graylog:2.5
USER root
COPY src/main/resources/docker/graylog/udp-input-graylog.json /usr/share/graylog/data/contentpacks/
ENV GRAYLOG_CONTENT_PACKS_AUTO_LOAD udp-input-graylog.json
ENV GRAYLOG_CONTENT_PACKS_LOADER_ENABLED true
ENV GRAYLOG_CONTENT_PACKS_DIR data/contentpacks