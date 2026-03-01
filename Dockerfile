FROM amazoncorretto:17-alpine

ENV PROFILES=${PROFILES}
ENV PORT=8080

COPY lebane-api/build/libs/*.jar /lebane/app.jar

WORKDIR /lebane
RUN chmod a+x app.jar

ENTRYPOINT exec java ${JAVA_OPTS} -jar app.jar --spring.profiles.active="$PROFILES"

EXPOSE ${PORT}