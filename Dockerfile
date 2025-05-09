ARG RUN_IMAGE=eclipse-temurin:21-jre-jammy
FROM ${RUN_IMAGE}

ARG TARGET_DIR=/opt/app
ARG SOURCE_DIR=build/distributions

COPY russian_trusted_root_ca.cer russian_trusted_root_ca.cer

COPY $SOURCE_DIR/*.tar application.tar
RUN mkdir $TARGET_DIR
RUN tar -xf application.tar -C $TARGET_DIR
RUN rm application.tar

ARG DOCKER_USER=app
RUN groupadd -r $DOCKER_USER && useradd -rg $DOCKER_USER $DOCKER_USER
USER $DOCKER_USER

EXPOSE 8081/tcp
EXPOSE 8086/tcp
CMD [ "/opt/app/application/bin/application" ]