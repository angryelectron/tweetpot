Tweetpot "tweets" when the kettle boils.  See
http://angryelectron.com/projects/tweetpot for hardware requirements, setup
instructions, and other project information. 

Install
--
1. Download the tweetpot distribution that matches your architecture.
2. Unzip the archive.  
3. Change to the hih4030 directory
4. Get a Twitter API key and tokens from http://dev.twitter.com
5. Add the key/tokens to twitter4j.properties
6. Assuming XBee is at /dev/ttyUSB0 and 9600, run "java -jar tweetpot.jar"
7. Run "java -jar tweetpot.jar -h" for other options.
 
Source
--
Get the source for this project from GitHub:

	 http://github.com/angryelectron/tweetpot

Build Tweetpot using Netbeans or from the command line using ant.  To create an
architecture specific distribution that contains native libraries for RXTX, 
run "ant -Dtarget=<target> jar" where target is one of {win32, win64,
linux-i686, linux-x64, mac}.

You may need to specify the java.library.path to the RXTX JNI to run the
application within Netbeans.  You can do this by navigating to the project's
Properties -> Run, then adding
"-Djava.library.path=./resources/<architecture>/<jni>". 
