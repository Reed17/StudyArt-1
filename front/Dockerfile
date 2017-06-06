FROM mhart/alpine-node:latest

COPY package.json /usr/src/study-art-front/package.json
WORKDIR /usr/src/study-art-front

VOLUME /usr/src/study-art-front

RUN npm install -g webpack
RUN npm install

EXPOSE 8081

CMD npm run dev
