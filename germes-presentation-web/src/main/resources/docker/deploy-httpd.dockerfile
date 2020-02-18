# Apache HTTP Server
FROM httpd:2

# Copy built angular project files to Apache server
COPY client/dist/ /usr/local/apache2/htdocs/
