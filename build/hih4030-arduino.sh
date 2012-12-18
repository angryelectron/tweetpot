#!/bin/bash
VERSION=1.0
DIR=../arduino
APP=hih4030-arduino

pushd $DIR
zip -r $APP-$VERSION.zip HIH4030 
popd
mv $DIR/$APP-$VERSION.zip .
