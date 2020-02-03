FROM node11.cc.ecsphere.net/common/openjdk:8-jdk-alpine-timezone-cn
COPY *.jar app.jar
# COPY *.ttc /usr/share/fonts/zh_CN/TrueType/
# COPY *.ttf /usr/share/fonts/zh_CN/TrueType/
CMD ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
