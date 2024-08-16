# 🚨 Educational Project Notice 🚨

**This project is for educational purposes only. It is not intended for use in real-world cryptographic applications. Do not use this implementation for securing sensitive data.**

---

# DSA Implementation Project

## DSA Implementation Project for Cryptography Course

This project involves the comprehensive implementation of the Digital Signature Algorithm (DSA) in Java. Guided by course material and standard documentation, the project begins with a thorough understanding of the DSA standard, developing functional signing and verification algorithms. Subsequent stages expand functionality to support various testing scenarios and performance evaluations, with a focus on clear documentation and code readability. The accompanying report includes user instructions, testing scripts and implementation insights.

## Table of Contents

1. [Introduction](#introduction)
2. [Installation and Setup](#installation-and-setup)
   - [Using Java Directly](#using-java-directly)
   - [Using Gradle](#using-gradle)
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
7. [FAQ](#faq)
   - [Common Errors and Solutions](#common-errors-and-solutions)

## Introduction

### Project Overview

This project aims to develop a implementation of the Digital Signature Algorithm (DSA), widely used for digital signatures. The primary goal is to provide a high-performance version of DSA supporting signing and verification of messages efficiently and "securely". Special focus is given to optimizing the signing and verification processes for performance evaluation.

### Context

Developed in an academic setting, this DSA project serves learning and research purposes. While rigorous, it may contain imperfections or bugs due to its experimental nature, such as the use of an extremely simple hash function.

## Installation and Setup

### Step 1: Download the Project

First, download the DSA project from the GitHub repository. You can clone the repository using the following command:

```sh
git clone https://github.com/cypri3/DSA-M1.git
```

Navigate to the project directory:

```sh
cd DSA-M1
```

### Step 2: Set Up the Environment

Once the project is downloaded, follow the steps below to set up the Java environment and Gradle build system.

#### Verifying Java Installation

1. **Verify Java Version**: Check if Java is installed and ensure it's the correct version:

    ```sh
    java -version
    ```

2. **Install JDK 17 if Necessary**:

    On Ubuntu/Debian, you can install JDK 17 with:

    ```sh
    sudo apt update
    sudo apt install openjdk-17-jdk
    ```

3. **Configure JAVA_HOME**: Set the `JAVA_HOME` environment variable:

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

### Step 3: Using Gradle

Gradle is used to build and run the project. Follow these steps:

#### Install Gradle

1. **Download and Install Gradle**:

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

2. **Generate the Gradle Wrapper**: If needed, regenerate the wrapper files:

    ```sh
    gradle wrapper
    ```

3. **Verify `gradle.properties` Configuration**: Ensure the Gradle properties are set up:

    ```sh
    echo 'org.gradle.java.home=/usr/lib/jvm/java-17-openjdk-amd64' > gradle.properties
    rm -rf ~/.gradle/caches/
    ```

### Step 4: Building the Project

Once everything is set up, clean and build the project using Gradle:

```sh
./gradlew clean build
```

Now, you can run, test, or clean the project using the corresponding Gradle commands:

- **Run the project**:

    ```sh
    ./gradlew run
    ```

- **Test the project**:

    ```sh
    ./gradlew test
    ```

- **Clean the project**:

    ```sh
    ./gradlew clean
    ```

NB : The test program verifies the correctness of the DSA implementation. It includes tests for key generation, message signing, and signature verification under various scenarios, including special characters and messages of different lengths.

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

- **DSA Key Pair Generation**: `generateKeyPair`
- **DSA Signing**: `signMessage`
- **DSA Verification**: `verifySignature`
- **Hashing Function**: `hash` - This function converts the message to a BigInteger and then takes the modulo \(2^{160}\) to produce a 160-bit hash.

### Implementation Particularities

#### Algorithms Used

- **Digital Signature Algorithm (DSA)**: The primary cryptographic algorithm used for signing and verification. DSA involves key pair generation, message signing, and signature verification.
- **Simplified Hash Function**: A simple modulo operation to extract the first 160 bits of the message as the hash, as suggested by the project guidelines. While not secure, it simplifies the implementation.

#### Implementation Choices

1. **BigInteger Library**: The Java `BigInteger` class is used for handling large integers and multiprecision arithmetic, crucial for cryptographic operations.
2. **SecureRandom**: Utilized for generating random values securely, essential for generating private keys and random values during signing.
3. **Parallelization**: Implemented using Java’s `ExecutorService` to perform signing and verification operations in parallel, leveraging multi-core processors to improve performance.

## Conclusion

### Summary

The project successfully implemented DSA in an academic context, emphasizing a clear and modular code structure, performance optimization, and adaptability to various testing environments. Well-defined data structures and comprehensive documentation facilitate understanding and potential future extensions.

## FAQ

### Common Errors and Solutions

#### Error: `./gradlew: /bin/sh^M: bad interpreter: No such file or directory`

This error occurs due to incorrect line endings in the `gradlew` script. To fix this, follow these steps:

1. Install `dos2unix`:
   ```sh
   sudo apt-get install dos2unix
   ```

2. Convert the `gradlew` file:
   ```sh
   dos2unix gradlew
   ```

3. Make the file executable:
   ```sh
   chmod +x gradlew
   ```

4. Run the script:
   ```sh
   ./gradlew run
   ```

These steps should resolve the `bad interpreter` issue and allow you to run Gradle correctly.

#### Error: `sudo: unzip: command not found`

This error occurs if the `unzip` utility is not installed on your system. To install it, run:
```sh
sudo apt-get install unzip
```

#### Error: `Error: Could not find or load main class org.gradle.wrapper.GradleWrapperMain`

This error can occur if the Gradle wrapper files are missing. To fix this, regenerate the wrapper files:
```sh
gradle wrapper
```

Ensure that the `gradle-wrapper.jar` file is present in the `gradle/wrapper/` directory before running the build command.
