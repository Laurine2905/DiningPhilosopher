package diningphilosophers;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
public class ChopStick {

    private static int stickCount = 0;
    private boolean iAmFree = true;
    private final int myNumber;
    public ChopStick() {
        myNumber = ++stickCount;
    }
    private final static Lock lock = new ReentrantLock();


    public boolean tryTake() throws InterruptedException {
        if (!iAmFree) {
            boolean locked = lock.tryLock(300, java.util.concurrent.TimeUnit.MILLISECONDS);
            if (!locked) // si c'est pas libre
            {
                return false; // Echec
            }
        }
        else{
            lock.lock();
        }
        // assert iAmFree;
        iAmFree = false;
        // Pas utile de faire notifyAll ici, personne n'attend qu'elle soit occupée
        return true; // Succès
    }

    public void release() {
        try {
            System.out.println("Stick " + myNumber + " Released");
            // assert iAmFree;
            iAmFree = true;
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public String toString() {
        return "Stick#" + myNumber;
    }
}
