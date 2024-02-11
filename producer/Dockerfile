FROM amazoncorretto:19

WORKDIR /app

# Копирование JAR-файла в контейнер
COPY producer/build/libs/producer-0.0.1-SNAPSHOT.jar producer.jar

# Команда для запуска JAR-файла
CMD ["java", "-jar", "producer.jar"]
