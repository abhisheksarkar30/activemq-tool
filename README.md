# activemq-tool
![](https://img.shields.io/badge/Release-V1.0.0-blue.svg) ![](https://img.shields.io/badge/Build-Stable-green.svg) [![License](https://img.shields.io/badge/License-Apache%202.0-red.svg)](https://opensource.org/licenses/Apache-2.0) ![](https://img.shields.io/badge/By-Abhishek%20Sarkar-red.svg?style=social&logo=appveyor)

------------


Uploades, Transfers, Downloads messages in bulk from/to Queue via OpenWire Connection(over TCP)

## Prerequisites
Java 7+, ActiveMQ 5.14.5+

## Usage
java -jar activemq-tool-1.jar or launch.bat(windows)

## Configuration
The tool doesn't take any CLI arguments, rather it works on the configuration.properties file under apps/conf directory.
Each parameter has its own comment to indicate its use. Broadly we are having these classifications supported by this tool:

### Actions
 - Upload
 - Transfer/Copy
 - Download
 
 ### Message Types
 - Binary
 - Text
 
 ### Text Message type Formats
 - XML
 - SWIFT
 
 ## Notes to follow
 Considering above classifications supported, below are the expectations:
  - This tool is specific to Queue operations, topics not yet considered/tested.
  - Binary messages are uploaded/downloaded one message per file but transfer all at a time.
  - Text messages for upload/downlod multiple messages per file(extracted and uploaded or downloaded into same file appended) and transfer all at a time.
  - Any new text message format to be added can be done easily maintaining the following config properties(in the file mentioned above):
    - message.starts.\<message-format> = \<top line begins with>
    - message.ends.\<message-format> = \<bottom line begins with>
