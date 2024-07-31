# 🚨 Educational Project Notice 🚨

**This project is for educational purposes only. It is not intended for use in real-world cryptographic applications. Do not use this implementation for securing sensitive data.**

---

# DSA Implementation Project

## DSA Implementation Project for Cryptography Course

This project involves the comprehensive implementation of the Digital Signature Algorithm (DSA) in Java. Guided by course material and standard documentation, the project begins with a thorough understanding of the DSA standard, developing functional signing and verification algorithms. Subsequent stages expand functionality to support various testing scenarios and performance evaluations, with a focus on clear documentation and code readability. The accompanying report includes user instructions, testing scripts and implementation insights.

## Table of Contents

1. [Introduction](#introduction)
2. [Installation and Setup](#installation-and-setup)
   - [Using Gradle](#using-gradle)
   - [Using Java Directly](#using-java-directly)
3. [Usage Guide](#usage-guide)
   - [Running the Program](#running-the-program)
4. [Test Program](#test-program)
   - [Description of the Test Program](#description-of-the-test-program)
   - [Results and Interpretation](#results-and-interpretation)
5. [Implementation Details](#implementation-details)
   - [Code Structure and Modules](#code-structure-and-modules)
   - [Main Functions](#main-functions)
   - [Implementation Particularities](#implementation-particularities)
6. [Conclusion](#conclusion)
   - [Summary](#summary)
   - [Future Improvements](#future-improvements)

## Introduction

### Project Overview

This project aims to develop a implementation of the Digital Signature Algorithm (DSA), widely used for digital signatures. The primary goal is to provide a high-performance version of DSA supporting signing and verification of messages efficiently and "securely". Special focus is given to optimizing the signing and verification processes for performance evaluation.

### Context

Developed in an academic setting, this DSA project serves learning and research purposes. While rigorous, it may contain imperfections or bugs due to its experimental nature, such as the use of an extremely simple hash function.

## Installation and Setup

### Using Gradle

To use the DSA project, Gradle is required. Follow these steps for installation:

#### Downloading and Installing Gradle on Linux

Download the specified version of Gradle:
```sh
wget https://github.com/gradle/gradle-distributions/releases/download/v8.0.2/gradle-8.0.2-bin.zip
sudo unzip gradle-8.0.2-bin.zip -d /opt/gradle
export PATH=/opt/gradle/gradle-8.0.2/bin:$PATH
source ~/.bashrc
```

Verify the installation:
```sh
gradle -v
```

#### Building the Project

Clean the project and build:
```sh
./gradlew clean
rm -rf ~/.gradle/caches/
./gradlew build
```

#### Main Commands

- Run the project:
  ```sh
  ./gradlew run
  ```
- Test the project:
  ```sh
  ./gradlew test
  ```
- Build the project:
  ```sh
  ./gradlew build
  ```
- Clean the project:
  ```sh
  ./gradlew clean
  ```

NB : The test program verifies the correctness of the DSA implementation. It includes tests for key generation, message signing, and signature verification under various scenarios, including special characters and messages of different lengths.


### Verifying Java installation

To check and configure Java installation, proceed as follows:

#### Verify Java Version

Check if Java is installed:
```sh
java -version
```

#### Install JDK 17 if Necessary (Ubuntu/Debian)

Install the required JDK version:
```sh
sudo apt update
sudo apt install openjdk-17-jdk
```

#### Configure JAVA_HOME

Set up the JAVA_HOME environment variable:
```sh
echo 'export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64' >> ~/.bashrc
echo 'export PATH=$JAVA_HOME/bin:$PATH' >> ~/.bashrc
source ~/.bashrc
```

Verify the setup:
```sh
echo $JAVA_HOME
java -version
```

#### Verify `gradle.properties` Configuration

Ensure the `gradle.properties` file is configured correctly:
```sh
echo 'org.gradle.java.home=/usr/lib/jvm/java-17-openjdk-amd64' > gradle.properties
```

#### Recompile the Project

Clean and build the project:
```sh
./gradlew clean build
```

## Usage Guide

### Running the Program with Different Modes

- **Test Mode**:
  ```bash
  ./gradlew run -PrunArgs="test,alice.txt"
  ```
- **Sign Mode**:
  ```bash
  ./gradlew run -PrunArgs="sign,alice.txt,signature.txt"
  ```
- **Verify Mode**:
  ```bash
  ./gradlew run -PrunArgs="verify,alice.txt,signature.txt"
  ```

## Test Program

### Description of the Test Program

The test program verifies the performance of the DSA implementation on 10,000 iterations of the process.

### Test Results

On running the test program:
```bash
./gradlew run -PrunArgs="test,alice.txt"
```

The following results were obtained:
```plaintext
Time for 10,000 signatures: 752 ms
Time for 10,000 verifications: 525 ms
Percentage of valid signatures: 100.0%
```

### System Specifications

The tests were run on a machine with the following specifications:
- **RAM**: 32 GB DDR5 6000 MHz
- **CPU**: i7-13700KF, 3.40 GHz
- **Operating System**: Windows 11 Famille - 23H2

## Implementation Details

### Code Structure and Modules

The project features a clear and modular code structure:

- **Main File**: `Main.java`: Entry point for the program, orchestrating the flow of signing and verification operations.
- **Test File**: `MainTest.java`: Contains test cases for key generation, message signing, and signature verification.

### Main Functions

- **DSA Key Pair Generation**
- **DSA Signing**
- **DSA Verification**
- **Hashing Function**

### Implementation Particularities

- **Use of Structures**: Enhances code readability and maintenance by organizing data into structures.
- **Multithreading**: Implemented at a high level to handle multiple signing and verification operations in parallel.
- **Code Readability and Documentation**: Emphasis on clear, readable code with comprehensive documentation.
- **Variety of Options**: Offers extensive configuration options to control various execution aspects.

## Conclusion

### Summary

The project successfully implemented DSA in an academic context, emphasizing a clear and modular code structure, performance optimization, and adaptability to various testing environments. Well-defined data structures and comprehensive documentation facilitate understanding and future extensions.

### Future Improvements

Potential enhancements include optimizing further for performance, improving the user interface, and conducting extensive security audits to ensure robustness and security in various use cases.