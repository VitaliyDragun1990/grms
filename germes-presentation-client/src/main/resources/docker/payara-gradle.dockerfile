# From here we'll get already built war archives configured specifically for Payara environment
FROM germes/base:payara AS  gradle6

# Here we'll put war archives
FROM ivonet/payara:5.194

# Path to the specific war archive to get
ARG war_path

# Copy specified war archive from germes/base to Payara autodeploy directory
COPY --from=gradle6 $war_path /opt/payara/payara5/glassfish/domains/domain1/autodeploy/