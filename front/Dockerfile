FROM mhart/alpine-node:latest

COPY package.json /usr/src/study-art-front/package.json
WORKDIR /usr/src/study-art-front

VOLUME /usr/src/study-art-front/

RUN npm install -g webpack

EXPOSE 8081

CMD npm install && npm run dev

# docker build -t front-image .
# docker run -it -v /home/v21k/dev/projects/StudyArt/front:/usr/src/study-art-front -p 8081:8081 front-image
