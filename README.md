# activemq-tool
![](https://img.shields.io/badge/Release-V1.0.0-blue.svg) ![](https://img.shields.io/badge/Build-Stable-green.svg) [![License](https://img.shields.io/badge/License-Apache%202.0-red.svg)](https://opensource.org/licenses/Apache-2.0) ![](https://img.shields.io/badge/By-Abhishek%20Sarkar-red.svg?style=social&logo=appveyor)

------------
Let's connect 👨‍💻 and forge the future together.😁✌

**Show your support a :star: is all this repo needs** :smile:
<br><br>

## Introduction
Uploads, Transfers/Copies, Downloads, Consumes messages in bulk from/to Queue via OpenWire Connection(over TCP/HTTP)

## Prerequisites
Java 7+, ActiveMQ 5.14.5+

## Usage
*java -jar activemq-tool-1.jar context apps/app-config.properties* or launch.bat(windows)

## Configuration
The tool doesn't take any CLI arguments, rather it works on the configuration.properties file under apps/conf directory.
Each parameter has its own comment to indicate its use. Broadly we are having these classifications supported by this tool:

### Actions
 - Upload (currently tested 25K text messages uploaded within 30sec from linux VM)
 - Transfer/Copy
 - Download
 - Consume
 
### Message Types
 - Binary
 - Text
 
### Text Message type Formats
 - XML
 - SWIFT
 - Default (Simple text only / unknown format)
 
## Notes to follow
 Considering above classifications supported, below are the expectations:
  - This tool is specific to Queue operations, topics not yet considered/tested.
  - Binary messages are uploaded/downloaded one message per file but transfer all at a time.
  - Text messages for upload/downlod multiple messages per file(extracted and uploaded or downloaded into same file appended) and transfer all at a time.
  - Single app can be used for multiple profiles, i.e. different context.xml, connection.properties & app-config.properties can be used for a single jar of this application. context files need to be within conf/ directory, app-config file can be anywhere in system, thus it requires path of the file.
  - Any new text message format to be added can be done easily maintaining the following config properties(in the file mentioned above, not required for Default type):
    - message.starts.\<message-format> = \<top line begins with>
    - message.ends.\<message-format> = \<bottom line begins with>
