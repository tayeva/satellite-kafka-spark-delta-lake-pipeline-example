#include "producer/kafka.h"
#include "producer/event_generated.h"
#include "producer/file_handler.h"
#include "producer/parse.h"
#include "producer/utils.h"

#include <chrono>
#include <future>
#include <iostream>
#include <mutex>
#include <random>
#include <thread>
#include <vector>

#include <flatbuffers/flatbuffers.h>
#include <kafka/KafkaProducer.h>

kafka::Value poc::build_kafka_payload(std::string &line, std::string file) {
  flatbuffers::FlatBufferBuilder builder;
  std::vector<double> data = poc::parse::data(line, ',');
  auto event = event::CreateEvent(
      builder, builder.CreateString(poc::extract_file_name(file)),
      poc::ns_timestamp(), data[0], data[1], data[2], data[3], data[4],
      data[5]);
  builder.Finish(event);
  uint8_t *buffer = builder.GetBufferPointer();
  int size = builder.GetSize();
  kafka::Value payload(buffer, size);
  return payload;
}

void poc::kafka_sample_producer(
    kafka::clients::producer::KafkaProducer &producer,
    const kafka::Topic &topic, std::string sample_file,
    std::pair<int, int> &milliseconds_range, std::mutex &mtx) {
  try {
    poc::io::FileHandler handler(sample_file);
    std::string line;
    auto delivery_callback =
        [](const kafka::clients::producer::RecordMetadata &metadata,
           const kafka::Error &error) {
          if (!error) {
            std::cout << "Message delivered: " << metadata.toString()
                      << std::endl;
          } else {
            std::cerr << "Message failed to be delivered: " << error.message()
                      << std::endl;
          }
        };
    handler.getline(line); // Skipping header
    while (handler.getline(line)) {
      std::lock_guard<std::mutex> lock(mtx);
      kafka::Value payload = poc::build_kafka_payload(line, sample_file);
      kafka::clients::producer::ProducerRecord record(topic, kafka::NullKey,
                                                      payload);
      producer.send(record, delivery_callback);
      std::this_thread::sleep_for(std::chrono::milliseconds(
          poc::random_value_between(milliseconds_range)));
    }
  } catch (const std::exception &e) {
    std::lock_guard<std::mutex> lock(mtx);
    std::cout << "Error opening file: " << e.what() << std::endl;
  }
}

void poc::kafka_async_sample_producer(
    kafka::clients::producer::KafkaProducer &producer,
    const kafka::Topic &topic, std::vector<std::string> &sample_files,
    std::pair<int, int> milliseconds_range) {
  std::vector<std::future<void>> futures;
  std::mutex mtx;
  for (const auto &f : sample_files) {
    futures.push_back(std::async(std::launch::async, [&producer, &mtx, &topic,
                                                      f,
                                                      &milliseconds_range]() {
      poc::kafka_sample_producer(producer, topic, f, milliseconds_range, mtx);
    }));
  }
  for (auto &future : futures) {
    future.wait();
  }
}