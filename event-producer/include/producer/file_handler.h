#ifndef POC_PRODUCER_FILE_HANDLER_H
#define POC_PRODUCER_FILE_HANDLER_H

#include <fstream>
#include <string>

namespace poc {
namespace io {

class FileHandler {
public:
  FileHandler(const std::string &file_name);
  ~FileHandler();
  bool getline(std::string &line);

private:
  std::ifstream file_;
};

} // namespace io
} // Namespace poc
#endif