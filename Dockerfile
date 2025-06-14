# Descarga la imagen de Docker de Eclipse Temurin con JDK 17
FROM eclipse-temurin:17.0.15_6-jdk

# Establece el puerto de trabajo
EXPOSE 8080

# Define el directorio de trabajo
WORKDIR /root

# Copia el pom y mvn
COPY ./pom.xml /root
COPY ./.mvn /root/.mvn
COPY ./mvnw /root

# Descarga las dependencias del proyecto
RUN ./mvnw dependency:go-offline

# Copia el código fuente del proyecto al contenedor
COPY ./src /root/src

# Compila el proyecto
RUN ./mvnw clean install -DskipTests

# levanto la aplicación
ENTRYPOINT ["java", "-jar", "/root/target/java2-0.0.1-SNAPSHOT.jar"]