# From here we'll get already built war archives
FROM germes/base AS  maven3

# Here we'll put war archives
FROM jetty:9.4.18-jre11

RUN rm -rf /var/lib/jetty/webapps/ROOT

# Path to the specific war archive to get
ARG war_path

# Copy specified war archive from germes/base to jetty webapps directory as ROOT applciation
COPY --from=maven3 $war_path /var/lib/jetty/webapps/ROOT.war