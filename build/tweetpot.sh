#!/bin/bash

APP=tweetpot
VERSION=1.0
DIR=../java/tweetpot

ARCH="win32 win64 linux-i686 linux-x64 mac"

rm -rf $APP-*-$VERSION.zip
ant -f $DIR/build.xml clean 
for arch in $ARCH
do
	ant -f $DIR/build.xml -Dtarget=$arch jar
	mv $DIR/$APP-$arch.zip $APP-$arch-$VERSION.zip	
done
