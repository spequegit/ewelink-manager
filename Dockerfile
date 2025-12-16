FROM eclipse-temurin:21-jre-alpine

# Opcjonalnie: nazwa użytkownika (zamiast root)
# RUN addgroup -S spring && adduser -S spring -G spring
# USER spring:spring

# Katalog roboczy w kontenerze
WORKDIR /app

# Skopiuj jar z Twojego projektu
# Jeśli nazwa pliku jest inna – zmień odpowiednio
COPY target/*.jar app.jar

# Opcjonalnie: jeśli chcesz mieć ładniejsze logi i lepszą wydajność
ENV JAVA_OPTS=""

# Port, na którym działa Twoja aplikacja (domyślnie Spring Boot = 8080)
EXPOSE 8080

# Uruchomienie aplikacji
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]