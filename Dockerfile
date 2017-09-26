FROM ubuntu:17.04
MAINTAINER Fatih Doğan "fatih.fd.dogan@gmail.com"

# Download openjdk-8-jdk
RUN apt-get -qq update && apt-get -qq install -y openjdk-8-jdk=8u131-b11-2ubuntu1.17.04.3

# Download wget
RUN apt-get -qq update && apt-get -qq install -y wget=1.18-2ubuntu1

# Download unzip
RUN apt-get -qq update && apt-get -qq install -y unzip=6.0-20ubuntu1

# Android SDK Tools r26.1.1
# For update: https://dl.google.com/android/repository/repository2-1.xml
RUN wget -q https://dl.google.com/android/repository/sdk-tools-linux-4333796.zip && \
    unzip -q sdk-tools-linux-4333796.zip -d android-sdk && \
    rm sdk-tools-linux-4333796.zip

# Android SDK Platform-Tools r26.0.0
# For update: https://dl.google.com/android/repository/repository2-1.xml
RUN wget -q https://dl.google.com/android/repository/platform-tools_r26.0.0-linux.zip && \
    unzip -q platform-tools_r26.0.0-linux.zip -d android-sdk && \
    rm platform-tools_r26.0.0-linux.zip

# Android SDK Build-Tools 26 r26.0.2
# For update: https://dl.google.com/android/repository/repository2-1.xml
RUN wget -q https://dl.google.com/android/repository/build-tools_r26.0.2-linux.zip && \
    unzip -q build-tools_r26.0.2-linux.zip -d android-sdk/build-tools && \
    mv android-sdk/build-tools/android-8.1.0/ android-sdk/build-tools/26.0.2 && \
    rm build-tools_r26.0.2-linux.zip

# Android SDK Platform 26 r2
# For update: https://dl.google.com/android/repository/repository2-1.xml
RUN wget -q https://dl.google.com/android/repository/platform-26_r02.zip && \
    unzip -q platform-26_r02.zip -d android-sdk/platforms && \
    mv android-sdk/platforms/android-8.0.0/ android-sdk/platforms/android-26 && \
    rm platform-26_r02.zip

# Set ANDROID_SDK_ROOT ANDROID_HOME
ENV ANDROID_SDK_ROOT=/android-sdk
ENV ANDROID_HOME=/android-sdk

# Set PATH
ENV PATH="/android-sdk/tools/:/android-sdk/tools/bin/:/android-sdk/platform-tools/:${PATH}"

# Disable Gradle Daemon
RUN mkdir -p ~/.gradle && echo "org.gradle.daemon=false" >> ~/.gradle/gradle.properties

CMD ./gradlew assembleDebug
