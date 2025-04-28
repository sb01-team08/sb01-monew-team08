FROM amazoncorretto:17-alpine AS builder

WORKDIR /app

# 빌드 설정 파일만 먼저 복사 (캐시 최적화)
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

# 소스 코드 복사
COPY src ./src
COPY config ./config

# gradlew 실행 가능하도록
RUN chmod +x ./gradlew

# 빌드
RUN ./gradlew clean bootJar --no-daemon

RUN cp build/libs/*.jar application.jar

RUN java -Djarmode=layertools -jar application.jar extract

FROM amazoncorretto:17-alpine
WORKDIR /app

COPY --from=builder /app/dependencies/ ./
COPY --from=builder /app/spring-boot-loader/ ./
COPY --from=builder /app/snapshot-dependencies/ ./
COPY --from=builder /app/application/ ./

ENV SERVER_PORT=8080
ENV SPRING_PROFILES_ACTIVE=prod

ENTRYPOINT ["java", "-Dserver.port=${SERVER_PORT}", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "org.springframework.boot.loader.launch.JarLauncher"]
