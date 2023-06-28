#/bin/bash

./third-party/flatbuffers/flatc --cpp ./event.fbs
mv event_generated.h event-producer/include/producer

./third-party/flatbuffers/flatc --java --java-package com.poc ./event.fbs
mv event/Event.java event-consumer/src/main/java/com/poc
rmdir event
