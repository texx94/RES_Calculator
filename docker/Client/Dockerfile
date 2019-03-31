FROM java:8

#
# When we build the image, we copy the executable jar in the image file system.
#
COPY Client.class /opt/app/client.class

#
# This is the command that is executed when the Docker container starts
#
CMD ["java", "/opt/app/client.class"]