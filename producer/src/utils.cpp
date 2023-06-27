#include "producer/utils.h"

#include <filesystem>
#include <random>

std::vector<std::string> poc::glob(std::string dir, std::string ext) {
  std::vector<std::string> files;
  std::filesystem::path dir_path = dir;
  for (const auto &entry : std::filesystem::directory_iterator(dir_path)) {
    if (entry.path().extension() == ext) {
      files.push_back(entry.path().string());
    }
  }
  return files;
}

unsigned long long poc::ns_timestamp() {
  auto current_time = std::chrono::system_clock::now();
  auto duration = current_time.time_since_epoch();
  return std::chrono::duration_cast<std::chrono::nanoseconds>(duration).count();
}

int poc::random_value_between(std::pair<int, int> &range) {
  std::random_device rd;
  std::mt19937 generator(rd());
  std::uniform_int_distribution<int> distribution(range.first, range.second);
  return distribution(generator);
}

std::string poc::extract_file_name(const std::string &file_path) {
  size_t last_slash_pos = file_path.find_last_of('/');
  std::string extracted_name = file_path.substr(last_slash_pos + 1);
  size_t ext_pos = extracted_name.find_first_of('.');
  if (ext_pos != std::string::npos) {
    extracted_name = extracted_name.substr(0, ext_pos);
  }
  return extracted_name;
}