FROM mhart/alpine-node:latest
EXPOSE 8081
RUN npm install -g webpack
COPY . /usr/src/study-art-front
WORKDIR /usr/src/study-art-front
RUN npm install
CMD npm run dev
