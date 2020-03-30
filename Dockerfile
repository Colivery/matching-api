FROM adoptopenjdk/openjdk11:slim
VOLUME /tmp
COPY target/engine-*.jar app.jar
ENTRYPOINT java -jar /app.jar --server.port=$PORT
