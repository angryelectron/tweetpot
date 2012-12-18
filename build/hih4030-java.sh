#!/bin/sh

VERSION=1.0

ant -f ../java/hih4030/build.xml
mv ../java/hih4030/hih4030-java.zip hih4030-java-$VERSION.zip
