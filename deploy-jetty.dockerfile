FROM jetty:9.4.26-jdk13-slim

# Remove default ROOT app
RUN rm -rf /var/lib/jetty/webapps/ROOT

# Path to the specific war archive to get
ARG war_path

COPY $war_path /var/lib/jetty/webapps/ROOT.war