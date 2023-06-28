cmake_minimum_required(VERSION 3.12)

project(poc)

set(CMAKE_CXX_STANDARD 17)

include_directories(/usr/local/include)
include_directories(${PROJECT_SOURCE_DIR}/../flatbuffers/include)
include_directories(${PROJECT_SOURCE_DIR}/include)

set(PRODUCER_SOURCES
    src/main.cpp
    src/file_handler.cpp
    src/kafka.cpp
    src/parse.cpp
    src/utils.cpp
)

add_executable(producer ${PRODUCER_SOURCES})

target_link_libraries(producer PUBLIC rdkafka rdkafka++)
