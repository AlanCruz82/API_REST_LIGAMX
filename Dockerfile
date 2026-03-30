#Imagen base que vamos a utilizar
FROM eclipse-temurin:17-jdk-jammy
#Directorio donde se va a almacenar nuestra aplicacion
WORKDIR /app
#Copiamos el .jar de nuestra app dentro de app_ligamx
COPY target/ligamx-0.0.1-SNAPSHOT.jar app_ligamx.jar
#Puerto donde se va a exponer nuestra aplicacion
EXPOSE 8080
#Comandos que se van a utilizar
ENTRYPOINT ["java","-jar","app_ligamx.jar"]