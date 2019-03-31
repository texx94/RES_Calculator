FROM java:8

#
# When we build the image, we copy the executable jar in the image file system.
#
COPY Client.jar /opt/app/client.jar

#
# This is the command that is executed when the Docker container starts
#
CMD ["java", "-jar", "/opt/app/client.jar"]