#!/usr/bin/env bash

folder=$1

mkdir $folder
cp -r gradle $folder/gradle
cp -r src $folder/src
cp build.gradle $folder/build.gradle
cp gradle.properties $folder/gradle.properties
cp gradlew $folder/gradlew
cp gradlew.bat $folder/gradlew.bat
cp settings.gradle $folder/settings.gradle
cp -r build/distributions $folder/executable

zip ${folder}_Aleksand_Izmaylov $folder -r
