package jboxGlue.Link;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;


/** 
 * MuscleWave is a class that handles a sin wave for
 * muscles to set their rest length. AT PRESENT, it
 * is in Environment Variables cause every muscle shares
 * However, it can be put into each muscle. 
 */
public class MuscleWave implements ActionListener {
    
    private final double DEFAULT_WAVE_SPEED = Math.toRadians(3);
    private final int DEFAULT_PERIOD = 20;
    private double myMussleWaveSpeed =  DEFAULT_WAVE_SPEED ;
    private double currentRadians = 0;
    private Timer timer = new Timer(DEFAULT_PERIOD, this);

    /**
     *  constructor of MuscleWave
     */
    public MuscleWave () {
        timer.setInitialDelay(0);
        timer.start();

    }

    /**
     * @param amplitude: amplitude
     * @param phase: phase
     * @return correct spring length according to sin wave
     */
    public double getSpringLength (double amplitude, double phase) {

        double resultsRadians = currentRadians + phase;
        return Math.sin(resultsRadians) * amplitude;
    }
    @Override
    /** call back of timer. Used to update the current radians of sin wave 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     * @param e : event
     */
    
    public void actionPerformed (ActionEvent e) {
        currentRadians += myMussleWaveSpeed;

    }

    /**
     * @param speed : the radian increased in each actionPerformed
     */
    public void setWaveSpeed (double speed) {
        myMussleWaveSpeed = speed;
    }

}
