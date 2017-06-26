FROM anapsix/alpine-java:8_jdk

COPY . /usr/src/study-art-server
WORKDIR /usr/src/study-art-server

RUN MAVEN_VERSION=3.3.3 \
 && apk add --update wget \
 && cd /usr/share \
 && wget --quiet http://archive.apache.org/dist/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz -O - | tar xzf - \
 && mv /usr/share/apache-maven-$MAVEN_VERSION /usr/share/maven \
 && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn \
 && apk del wget

ENV M2_HOME /usr/share/maven

VOLUME /usr/src/study-art-server/db
VOLUME /usr/src/study-art-server/logs
VOLUME /usr/src/study-art-server/dependencies
VOLUME $HOME/.m2

EXPOSE 8080
EXPOSE 5005

CMD mvn clean package -DskipTests=true \
&& cd target/ \
&& java -jar -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 server-1.0.jar \
--application.courses.paths.dependencies=/usr/src/study-art-server/dependencies \
--spring.datasource.url=jdbc:h2:file:/usr/src/study-art-server/db/studyArt;DB_CLOSE_ON_EXIT=FALSE

# docker build -t server-image .

#docker run -it -v /home/v21k/dev/projects/StudyArt/db:/usr/src/study-art-server/db -v /home/v21k/dev/projects/StudyArt/logs:/usr/src/study-art-server/logs -v /home/v21k/dev/projects/StudyArt/server/dependencies:/usr/src/study-art-server/dependencies -v $HOME/.m2:/root/.m2 -p 8080:8080 -p 5005:5005 study-server
