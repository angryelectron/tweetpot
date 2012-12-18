#!/bin/sh

PIDFILE=/var/run/tweetpot.pid
DESC=tweetpot
DAEMON="/usr/bin/java"
DAEMON_ARGS="-jar tweetpot.jar"
DIR=/opt/tweetpot

is_running() {
	if [ -f $PIDFILE ]; then
		PID=`cat $PIDFILE`
		if [ -n "$PID" ]; then
			return 0
		else
			return 1
		fi
	else
		return 1
	fi
}

start_zort() {
	if ! is_running; then
		echo "Starting $DESC"
		cd $DIR
		start-stop-daemon --start --make-pidfile --pidfile $PIDFILE --chdir $DIR --exec $DAEMON -- $DAEMON_ARGS 2>&1 &
		sleep 1;
		if is_running; then
			echo "$DESC: running, pid $PID"
		else
			echo "$DESC: could not start"
		fi
	else
		echo "$DESC: already running (pid $PID)"
	fi
}

stop_zort() {
	if is_running; then
		echo "Stopping $DESC"
		start-stop-daemon --stop --pidfile $PIDFILE --signal 15
		rm $PIDFILE
	else
		echo "$DESC is not running."
	fi
}

case "$1" in
	start)
		start_zort
	;;
	stop)
		stop_zort
	;;
	restart)
		stop_zort
		start_zort
	;;
	*)
		echo "Usage: $0 {start|stop|restart}"
		exit 3
	;;
esac
