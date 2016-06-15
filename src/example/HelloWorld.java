package example;
/* Periodic thread in Java */

import javax.realtime.PriorityScheduler;
import javax.realtime.PriorityParameters;
import javax.realtime.PeriodicParameters;
import javax.realtime.RelativeTime;
import javax.realtime.RealtimeThread;

public class HelloWorld {
  public static void main(String[] args)
  {
    /* priority for new thread: mininum+10 */
    int priority = PriorityScheduler.instance().getMinPriority()+10;
    PriorityParameters priorityParameters = new PriorityParameters(priority);

    /* period: 200ms */
    RelativeTime period = new RelativeTime(200 /* ms */, 0 /* ns */);

    /* release parameters for periodic thread: */
    PeriodicParameters periodicParameters = new PeriodicParameters(null,period, null,null,null,null);

    /* create periodic thread: */
    RealtimeThread realtimeThread = new RealtimeThread(priorityParameters,periodicParameters)
    {
      public void run()
      {
        for (int n=1;n<50;n++)
        {
          waitForNextPeriod();
          System.out.println("Hello " + n);
        }
      }
    };

    /* start periodic thread: */
    realtimeThread.start();
  }
}