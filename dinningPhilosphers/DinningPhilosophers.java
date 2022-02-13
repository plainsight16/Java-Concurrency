import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

public class DinningPhilosophers {
    static int no_of_philosophers = 5;
    static Philosopher []philosophers = new Philosopher [no_of_philosophers];
    static Chopstick []chopsticks = new Chopstick[no_of_philosophers];


    private static class Chopstick{
        public Semaphore semaphore = new Semaphore(1);
        void grabChopstick(){
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        void dropChopstick(){
            semaphore.release();
        }
    }
    private static class Philosopher implements Runnable{
        private int number;
        Chopstick leftChopstick;
        Chopstick rightChopstick;
        public Philosopher(int num, Chopstick left, Chopstick right){
            number = num;
            leftChopstick = left;
            rightChopstick = right;
        }
        @Override
        public void run(){
            while(true) {
                leftChopstick.grabChopstick();
                System.out.println("Philosopher " + (number + 1) + " picks up leftChopstick");
                rightChopstick.grabChopstick();
                System.out.println("Philosopher " + (number + 1) + " picks up rightChopstick");
                eat();
                leftChopstick.dropChopstick();
                System.out.println("Philosopher " + (number + 1) + " drops leftChopstick");
                rightChopstick.dropChopstick();
                System.out.println("Philosopher " + (number + 1) + " drops rightChopstick");
            }
        }
        void eat(){
            try {
                int eatTime = ThreadLocalRandom.current().nextInt(0, 1000);
                System.out.println("Philosopher " + (number + 1) + " eats for " + eatTime + " ms");

                Thread.sleep(eatTime);
            }catch(InterruptedException ex){
                ex.printStackTrace();
            }
        }
    }
    public static void main(String []args){
        for (int i = 0; i < no_of_philosophers; i++){
            chopsticks[i] = new Chopstick();
        }
        ExecutorService executor = Executors.newFixedThreadPool(no_of_philosophers);
        for (int i = 0; i < no_of_philosophers; i++){
            if( i == (no_of_philosophers - 1))
                philosophers[i] = new Philosopher(i, chopsticks[(i + 1) % no_of_philosophers], chopsticks[i] );
            else
                philosophers[i] = new Philosopher(i, chopsticks[i], chopsticks[(i + 1) % no_of_philosophers] );

            executor.execute(philosophers[i]);
        }
    }
}

