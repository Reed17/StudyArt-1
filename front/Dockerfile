FROM mhart/alpine-node:latest
RUN npm install -g webpack
COPY . /usr/src/study-art-front
WORKDIR /usr/src/study-art-front
RUN npm install
EXPOSE 8081
CMD npm run dev
