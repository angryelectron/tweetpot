Tweetpot
========
Tweetpot "tweets" when the kettle boils.  See
http://angryelectron.com/projects/tweetpot for hardware requirements, setup
instructions, and other project information. 

Pre-Requisites
--------------
* Tweepot hardware
* Java SDK
* Apache Ant
* Git (optional)

Build
-----
1. Visit https://github.com/angryelectron.com/tweetpot 
2. Get the source using a git client or by clicking the "Download ZIP" button. 
3. Unzip and/or change to ./tweetpot/java/tweetpot 
4. Run "ant jar -Dtarget=[win32|win64|linux-i686|linux-x64|mac]"

For example, "ant jar -Dtarget=win64" will install the correct RXTX
native libraries to use tweetpot on 64-bit windows and build an archive called
tweetpot-win64.zip.  

Configure
---------
1. Unzip tweetpot-[arch].zip, then change to tweetpot-[arch].
1. Get a Twitter API key and tokens from http://dev.twitter.com
2. Add the key/tokens to twitter4j.properties
3. Assuming XBee is at /dev/ttyUSB0 and 9600, run "java -jar tweetpot.jar"
4. Run "java -jar tweetpot.jar -h" for other options.
 
Netbeans
--------
You may need to specify the java.library.path to the RXTX JNI to run the
application within Netbeans.  You can do this by navigating to the project's
Properties -> Run, then adding
"-Djava.library.path=./resources/[architecture]/[jni]". 
