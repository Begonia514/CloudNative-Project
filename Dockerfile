FROM eclipse-temurin:8u372-b07-jre-centos7

ADD ./target/hello-service.jar /app/hello-service.jar

ADD runboot.sh /app/

WORKDIR /app

RUN chmod a+x runboot.sh

CMD ["sh","-c","/app/runboot.sh"]