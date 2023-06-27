#include "producer/parse.h"

#include <iostream>
#include <sstream>
#include <string>
#include <vector>

std::vector<std::string> poc::parse::header(std::string &line, char delimiter) {
  std::vector<std::string> values;
  std::string token;
  std::istringstream token_stream(line);
  while (std::getline(token_stream, token, delimiter)) {
    try {
      values.push_back(token);
    } catch (const std::exception &e) {
      std::cout << "Failed to parse token: " << token << std::endl;
    }
  }
  return values;
}

std::vector<double> poc::parse::data(std::string &line, char delimiter) {
  std::vector<double> values;
  std::string token;
  std::istringstream token_stream(line);
  while (std::getline(token_stream, token, delimiter)) {
    try {
      double value = std::stod(token);
      values.push_back(value);
    } catch (const std::exception &e) {
      std::cout << "Failed to parse token: " << token << std::endl;
    }
  }
  return values;
}