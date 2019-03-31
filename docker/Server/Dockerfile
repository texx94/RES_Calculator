FROM java:8

#
# When we build the image, we copy the executable jar in the image file system.
#
COPY CalculatorServer.class /opt/app/server.class

EXPOSE 2205

#
# This is the command that is executed when the Docker container starts
#
CMD ["java", "/opt/app/server.class"]