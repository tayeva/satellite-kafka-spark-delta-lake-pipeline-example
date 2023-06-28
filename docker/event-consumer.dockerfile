FROM openjdk:17-jdk-slim

WORKDIR /app

RUN apt-get update && \
    apt-get install -y curl && \
    curl -L sbt.zip https://github.com/sbt/sbt/releases/download/v1.9.0/sbt-1.9.0.tgz | tar -xz -C /usr/local && \
    ln -s /usr/local/sbt/bin/sbt /usr/local/bin/sbt

COPY event-consumer /app

CMD ["/bin/bash"]
