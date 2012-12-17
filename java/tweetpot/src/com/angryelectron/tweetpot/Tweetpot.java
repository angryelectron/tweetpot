package com.angryelectron.tweetpot;

import com.angryelectron.han.HanEventHandler;
import com.angryelectron.han.HumiditySensor;
import com.rapplogic.xbee.api.XBeeException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class Tweetpot {

    private HumiditySensor hygrometer;
    private enum KettleState {COLD, HOT};
    private KettleState state;
    
    /**
     * Constructor.
     * @param port
     * @param baud
     * @throws XBeeException 
     */
    public Tweetpot(String port, Integer baud) throws XBeeException {
        state = KettleState.COLD;
        hygrometer = new HumiditySensor(onHumidityReading);
        hygrometer.start(port, baud);
    }
    
    /**
     * Handle events that occur when humidity readings are received.  This is
     * passed to the humidity sensor by the constructor.
     */
    private HanEventHandler onHumidityReading = new HanEventHandler() {

        @Override
        public void onSensorReading(Object obj) {
            switch (state) {
                case COLD:
                    if (hygrometer.getRelativeHumidity() > 80) {
                        changeState(KettleState.HOT);
                        tweet();
                    }
                    break;
                case HOT:
                    if (hygrometer.getRelativeHumidity() < 60) {
                        changeState(KettleState.COLD);
                    }
                    break;
            }
            Logger.getLogger(Tweetpot.class.getName()).log(Level.DEBUG, hygrometer.getRelativeHumidity());
        }

        @Override
        public void onError(Object obj) {
            HumiditySensor hygrometer = (HumiditySensor) obj;
            hygrometer.stop();
            System.exit(1);
        }
    };
        
    /**
     * Send a tweet using the application and auth credentials specified
     * in twitter4j.properties.  See http://dev.twitter.com to obtain the
     * required api keys.
     */
    private void tweet() {        
        SimpleDateFormat df = new SimpleDateFormat("h:mm:ss a");
        String message = "The kettle has boiled (" + df.format(new Date()) + ")";
        Twitter twitter = new TwitterFactory().getInstance();
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, message);
        try {
            twitter.updateStatus(message);
        } catch (TwitterException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.ERROR, ex);
        }
    }

    /**
     * Change states
     */
    private void changeState(KettleState state) {
        this.state = state;
        Logger.getLogger(this.getClass().getName()).log(Level.DEBUG, "New state = " + state.toString());
    }
}
