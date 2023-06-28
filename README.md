# satellite-kafka-spark-delta-lake-pipeline-example

This is an example/demo ("proof of concept") project. It is a producer-consumer program with randomly generated data. The premise is that there are multiple satellites producing data asynchronously. The data is published to a message broker, then processed and written to storage with an extract-tranform-load (ETL) pipeline.

## Architecture

- Sample generation code - Python >3.7 (`pip install -r requirements.txt`)
    - `sample_data.py`
- Serialization - [FlatBuffers](https://github.com/google/flatbuffers)
- Producer code - C++17
    - [librdkafka](https://github.com/confluentinc/librdkafka/tree/master)
    - [modern-cpp-kafka](https://github.com/morganstanley/modern-cpp-kafka)
- Message broker - Kafka (standalone)
    - Zookeeper (standalone)
- Consumer code - Scala/Spark and Java
- Storage: [Delta Table](https://delta.io)
    - Parquet w/ Snappy compression
- Deployment - [Docker](https://www.docker.com) and [docker-compose](https://docs.docker.com/compose/)
- Figures - Python >3.7 (`pip install -r requirements.txt`)
    - `figures.ipynb`

### Sample data

The sample data is a python script that creates simulated satellite data.

A "satellite sample" is contained in one csv, so one csv per satellite. The "name" of the satellite is the file name. For example, here is the head of `sat0.csv`.

temp_c,battery_charge_pct,altitude,sensor1,sensor2,sensor3<br>
17.74,54.09,786.75,0.42,0.52,0.47<br>
27.58,44.02,1251.02,0.42,0.51,0.5<br>
-8.67,46.33,1270.95,0.5,0.45,0.52<br>
29.25,47.63,1818.88,0.47,0.49,0.51

There is no index in the files because as the data is pumped through the system a nanosecond time stamp is generated.

Fields
- temp_c
- battery_charge_pct
- altitude
- sensor1
- sensor2
- sensor3

Writes data to `./data/samples`.

### Data serialization

The serialization format chosen is Google Flatbuffers. Flatbuffers, are similar to Google Protocol Buffers ("Protobufs"). They have various trade-offs. Flatbuffers tend to be slightly larger (bytes) than Protobufs, but Flatbuffers offers zero-copy deserialization.

### Producer

This process is written in C++17. The producer process reads all of the files asynchronously (`async`) using multiple threads. After the data is read and parsed from the csv file, it is serialized, then published to a Kafka topic called `sat-data`. Between each event the program randomly sleeps between a given range to simulate the "live" data.

### Consumer

The consumer process utilizes Spark streaming in Scala. It consumes the `sat-data` kafka topic, accumulating the events in micro-batch fashion; collecting events during a periodic window (`5s`). Once the events are collected in an RDD (resilient distributed dataset), the flatbuffers are deserialized (Java flatbuffers) and the received timestamp is collected from Kafka. Using this data, a dataframe is created and appended to a Delta Table, storing the structured data as Parquet files with Snappy compression.

Writes data to `./data/delta-tables/satellite-data-raw`.

## Deployment

Docker in combination with docker-compose to deployment the project. It is a simple way to deploy that ensure consistent behavior across platforms.

Start:
`docker-compose up`

Stop:
`docker-compose down`

## Rebuilding flatbuffers files (dev-only)
To rebuild the flatbuffers generated files:

Build the flatbuffers `flatc` binary:

`./build-flatc.sh`

Run the following script to build the `event.fbs` flatbuffers schema for C++ and Java:

`./build-event-flatbuffers.sh`


## Notes on third-party

These git submodules are included for development purposes. They are not necessary deployment.