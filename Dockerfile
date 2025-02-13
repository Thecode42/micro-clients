FROM tomcat:jdk17

WORKDIR /usr/local/tomcat/webapps

COPY target/*.war clientes.war

EXPOSE 8080

CMD ["catalina.sh", "run"]