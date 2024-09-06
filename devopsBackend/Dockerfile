FROM eclipse-temurin:21-jre-alpine
LABEL maintainer="uzz99 <yj.lee991228@gmail.com>"
COPY ./build/libs/pocari-0.0.1-SNAPSHOT.jar /root
ENV TZ=Asia/Seoul
EXPOSE 8080
WORKDIR /root
CMD [ "java", "-jar", "/root/pocari-0.0.1-SNAPSHOT.jar"]