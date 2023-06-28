#include "producer/kafka.h"
#include "producer/utils.h"

#include <chrono>
#include <iostream>
#include <string>
#include <thread>
#include <vector>

#include <kafka/KafkaProducer.h>

int main(int argc, char *argv[]) {
  if (argc != 2) {
    std::cout << "Usage: main <directory>" << std::endl;
    return EXIT_FAILURE;
  }
  std::this_thread::sleep_for(std::chrono::seconds(60));
  std::vector<std::string> files = poc::glob(argv[1], ".csv");
  const std::string brokers = "kafka:9092";
  const kafka::Topic topic = "sat-data";
  const kafka::Properties props({{"bootstrap.servers", brokers}});
  kafka::clients::producer::KafkaProducer producer(props);
  poc::kafka_async_sample_producer(producer, topic, files,
                                   std::make_pair(100, 200));
  return EXIT_SUCCESS;
}
