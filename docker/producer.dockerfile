FROM tayeva/modern-cpp-kafka:2.1.1-v2023.03.07

WORKDIR /app

RUN apt-get install -y git \
    && git clone https://github.com/google/flatbuffers.git \
    && cd /app/flatbuffers \
    && cmake -G "Unix Makefiles" \
    && make -j

COPY event-producer event-producer

RUN cd event-producer \
    && mkdir build \
    && cd build \
    && cmake .. \   
    && cmake --build .

CMD ["./event-producer/build/producer", "./samples"]