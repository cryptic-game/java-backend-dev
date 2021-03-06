ARG JAVA_VERSION=16
ARG JVM_IMPL=hotspot
ARG SERVICE_NAME=server

FROM adoptopenjdk:${JAVA_VERSION}-jdk-${JVM_IMPL} AS builder

ARG SERVICE_NAME=server

RUN set -o errexit -o nounset \
 && apt-get update \
 && apt-get install git -y

COPY . /src
WORKDIR /src

RUN chmod +x ./gradlew \
    && ./gradlew :${SERVICE_NAME}:bootJar --stacktrace --no-daemon

FROM adoptopenjdk:${JAVA_VERSION}-jre-${JVM_IMPL} AS extractor

ARG SERVICE_NAME=server

WORKDIR /src

COPY --from=builder /src/${SERVICE_NAME}/build/libs/${SERVICE_NAME}*.jar cryptic.jar
RUN java -Djarmode=layertools -jar cryptic.jar extract

FROM adoptopenjdk:${JAVA_VERSION}-jre-${JVM_IMPL}

ENV HTTP_PORT=8080
ENV MANAGEMENT_PORT=8081

ARG CRYPTIC_USER=cryptic
ARG CRYPTIC_GROUP=cryptic
ARG DATA_DIR=/data

RUN set -o errexit -o nounset \
 && groupadd --system --gid 1000 ${CRYPTIC_GROUP} \
 && useradd --system --gid ${CRYPTIC_GROUP} --uid 1000 --shell /bin/bash --create-home ${CRYPTIC_USER} \
 && mkdir -p ${DATA_DIR} \
 && chown --recursive ${CRYPTIC_USER}:${CRYPTIC_GROUP} ${DATA_DIR} \
 && chown --recursive ${CRYPTIC_USER}:${CRYPTIC_GROUP} /home/${CRYPTIC_USER} \
 \
 && apt-get update \
 && apt-get install -y --no-install-recommends curl jq \
 && rm -rf /var/lib/apt/lists/*

WORKDIR ${DATA_DIR}

USER ${CRYPTIC_USER}:${CRYPTIC_GROUP}
VOLUME ${DATA_DIR}

EXPOSE ${HTTP_PORT}
EXPOSE ${MANAGEMENT_PORT}

COPY --from=extractor /src/dependencies/ /opt/cryptic
COPY --from=extractor /src/spring-boot-loader/ /opt/cryptic
COPY --from=extractor /src/snapshot-dependencies/ /opt/cryptic
COPY --from=extractor /src/application/ /opt/cryptic

ENTRYPOINT ["java", "-cp", "/opt/cryptic", "org.springframework.boot.loader.JarLauncher"]

HEALTHCHECK --start-period=10s --interval=30s --timeout=3s --retries=3 \
            CMD curl --silent --fail --request GET "http://localhost:${MANAGEMENT_PORT}/actuator/health" \
                | jq --exit-status '.status == "UP"' || exit 1
