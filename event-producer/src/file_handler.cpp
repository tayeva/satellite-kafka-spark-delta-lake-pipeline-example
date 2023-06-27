#include "producer/file_handler.h"

poc::io::FileHandler::FileHandler(const std::string &file_name)
    : file_(file_name) {
  if (!file_) {
    throw std::runtime_error("Error opening file: " + file_name);
  }
}

poc::io::FileHandler::~FileHandler() {
  if (file_.is_open()) {
    file_.close();
  }
}

bool poc::io::FileHandler::getline(std::string &line) {
  return static_cast<bool>(std::getline(file_, line));
}
