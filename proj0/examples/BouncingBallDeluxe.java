/******************************************************************************
 *  Compilation:  javac BouncingBallDeluxe.java
 *  Execution:    java BouncingBallDeluxe
 *  Dependencies: StdDraw.java StdAudio.java
 *                http://www.cs.princeton.edu/introcs/15inout/TennisBall.png
 *                http://www.cs.princeton.edu/introcs/15inout/pipebang.wav
 *
 *  Implementation of a bouncing tennis ball in the box from (-1, -1) to (1, 1),
 *  with sound effects.
 *
 *  % java BouncingBallDeluxe
 *
 ******************************************************************************/

public class BouncingBallDeluxe { 
    public static void main(String[] args) {

        // initial values
        double rx = 0.480, ry = 0.860;     // position
        double vx = 0.015, vy = 0.023;     // velocity
        double radius = 0.05;              // radius

        // set the scale of the coordinate system
        StdDraw.setXscale(-1.0, 1.0);
        StdDraw.setYscale(-1.0, 1.0);
        StdDraw.enableDoubleBuffering();

        // main animation loop
        while (true) { 

            // bounce off wall according to law of elastic collision
            if (Math.abs(rx + vx) + radius > 1.0) {
                vx = -vx;
              //  StdAudio.play("pipepang.wav");
            }
            if (Math.abs(ry + vy) + radius > 1.0) {
                vy = -vy;
               // StdAudio.play("pipepang.wav");
            }

            // update position
            rx = rx + vx; 
            ry = ry + vy; 

            // set the background to light gray
            StdDraw.clear(StdDraw.LIGHT_GRAY);

            // draw ball on the screen
            StdDraw.picture(rx, ry, "TennisBall.gif");

            // display and pause for 20ms
            StdDraw.show();
            StdDraw.pause(20); 
        } 
    } 
} 