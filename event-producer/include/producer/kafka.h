#ifndef POC_PRODUCER_KAFKA_H
#define POC_PRODUCER_KAFKA_H

#include <kafka/KafkaProducer.h>
#include <mutex>
#include <string>
#include <vector>

namespace poc {

kafka::Value build_kafka_payload(std::string &line, std::string file);

void kafka_sample_producer(kafka::clients::producer::KafkaProducer &producer,
                           const kafka::Topic &topic, std::string file,
                           std::pair<int, int> &milliseconds_range,
                           std::mutex &mtx);

void kafka_async_sample_producer(
    kafka::clients::producer::KafkaProducer &producer,
    const kafka::Topic &topic, std::vector<std::string> &sample_files,
    std::pair<int, int> milliseconds_range);

} // Namespace poc
#endif