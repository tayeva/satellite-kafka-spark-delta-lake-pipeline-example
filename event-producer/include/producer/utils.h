#ifndef POC_PRODUCER_UTILS_H
#define POC_PRODUCER_UTILS_H

#include <string>
#include <vector>

namespace poc {

std::vector<std::string> glob(std::string dir, std::string ext);

unsigned long long ns_timestamp();

int random_value_between(std::pair<int, int> &range);

std::string extract_file_name(const std::string &file_path);

} // Namespace poc
#endif