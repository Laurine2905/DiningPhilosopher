package diningphilosophers;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Philosopher
        extends Thread {

    private static int seed = 1;
    private final Random myRandom = new Random(System.currentTimeMillis() + seed++);
    private final static int DELAY = 1000;
    private final ChopStick myLeftStick;
    private final ChopStick myRightStick;
    private boolean running = true;
    private final String myName;

    public Philosopher(String name, ChopStick left, ChopStick right) {
        myName= name;
        myLeftStick = left;
        myRightStick = right;
    }

    @Override
    public void run() {
        while (running) {
            try {
                think();
                if (tryTakeStick(myLeftStick)) {
                    if (tryTakeStick(myRightStick)) {
                        // success : process
                        eat();
                        // release resources
                        myLeftStick.release();
                        myRightStick.release();
                    } else {
                            // failure : release resources
                            releaseStick(myLeftStick);
                        }
                    }
                    // try again
                } catch (InterruptedException ex) {
                    Logger.getLogger("Table").log(Level.SEVERE, "{0} Interrupted", this.getName());
                }
            }
            System.out.println(myName + " leaves table");

        }

        // Permet d'interrompre le philosophe "proprement" :
    // Il relachera ses baguettes avant de s'arrêter
    public void leaveTable() {
        running = false;
    }

    private void think() throws InterruptedException {
        int delay = myRandom.nextInt(500 + DELAY);
        System.out.println(this.getName() + " Starts Thinking for: " + delay + " ms");
        try {
            sleep(delay);
        } catch (InterruptedException ex) {
        }
        System.out.println(this.getName() + " Stops Thinking");
    }

    private void eat() throws InterruptedException {
        int delay = myRandom.nextInt(100 + DELAY);
        System.out.println(this.getName() + " Starts Eating for:" + delay + " ms");
        try {
            sleep(delay);
        } catch (InterruptedException ex) {
        }
        System.out.println(this.getName() + " Stops Eating");
    }
    private boolean tryTakeStick(ChopStick stick) throws InterruptedException {
        int delay = myRandom.nextInt(100 + DELAY);
        boolean result = stick.tryTake();
        if (result) {
            System.out.println(myName + " took " + stick + " before " + delay + " ms");
        } else {
            System.out.println(myName + " could not take " + stick + " before " + delay + " ms");
        }
        return result;
    }
    private void releaseStick(ChopStick stick) {
        stick.release();
        System.out.println(myName + " releases " + stick);
    }

}
