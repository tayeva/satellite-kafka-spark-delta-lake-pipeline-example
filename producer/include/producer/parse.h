#ifndef POC_PRODUCER_PARSE_H
#define POC_PRODUCER_PARSE_H

#include <string>
#include <vector>

namespace poc {
namespace parse {

std::vector<std::string> header(std::string &line, char delimiter);

std::vector<double> data(std::string &line, char delimiter);

} // namespace parse
} // namespace poc
#endif
