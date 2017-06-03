FROM anapsix/alpine-java:latest
COPY /target/server-1.0.jar /usr/src/study-art-front/server-1.0.jar
WORKDIR /usr/src/study-art-front
EXPOSE 8080
CMD java -jar server-1.0.jar
# todo DB volume