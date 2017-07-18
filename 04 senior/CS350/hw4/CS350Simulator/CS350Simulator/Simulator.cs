using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace CS350Simulator
{
    class Simulator
    {
        #region SIMULATION SETTINGS
        public SystemType sysType;
        public int seed;
        double requestedEndTime;
        double simEndTime;
        public int step;
        public double time;
        Random rand;
        public bool exportQueueToFile = false;
        #endregion

        #region CPU SETTINGS
        double cpu_Lambda;
        ServiceTime cpu_Ts;
        int cpu_K = 0; // 0 = infinite queue
        #endregion

        #region CPU VARIABLES
        public PriorityQueue<double, Event> schedule;
        public LinkedList<Request> queue;
        public int ticketNumber = 0;
        public int requestsProcessed = 0;
        public double totalResponseTime = 0;
        public double totalWaitTime = 0;
        public int totalInQueue = 0;
        public int totalInSystem = 0;
        public int rejectCount = 0;
        public int totalObservations = 0;
        public LinkedList<Monitor> observations;
        #endregion

        #region CONSTANTS
        const double ZSCORE_95TH = 1.96;
        const double ZSCORE_98TH = 2.33;
        const double ZSCORE_99TH = 2.58;
        #endregion

        public Simulator(SystemType sysType, double lambda, double totalTime, ServiceTime Ts, int queueSize)
        {
            // set simulation time to 0
            this.time = 0;

            // set simulation settings
            this.sysType = sysType;
            this.cpu_Lambda = lambda;
            this.cpu_Ts = Ts;
            this.requestedEndTime = totalTime;
            this.simEndTime = 2 * totalTime;
            this.cpu_K = queueSize;

            // initialize variables
            this.schedule = new PriorityQueue<double, Event>();
            this.queue = new LinkedList<Request>();
            this.observations = new LinkedList<Monitor>();

            // make a new Random number generator and remember the seed
            seed = (int)DateTime.Now.Ticks;
            rand = new Random(seed);

            // initialize schedule by adding a single birth
            schedule.Enqueue(0.0, new Birth(ticketNumber++, 0.0));

            // also add a monitor event (interarrival time is exponentially distributed with mean of 1s)
            double interarrivalTime = this.nextExponentialDouble(1, rand.NextDouble());
            schedule.Enqueue(0.0, new Monitor(interarrivalTime));
        }

        /// <summary>
        /// Performs a step of the simulation and increases the step counter by 1.
        /// </summary>
        /// <returns>A boolean which represents whether the simulation is over or not.</returns>
        public bool Step()
        {
            // guard against stepping past simulation end time
            if (time >= simEndTime)
            {
                return false;
            }

            // get the next event from the schedule
            Event evt = schedule.DequeueValue();

            // update the simulation time
            time = evt.scheduledTime;

            // process it
            if (evt is Birth)
            {
                // check if this simulator is an M/M/1/K
                if (cpu_K > 0 && queue.Count >= cpu_K)
                {
                    rejectCount++; // reject the request if queue is filled
                }
                else
                {
                    // add request to request queue
                    Request newRequest = new Request(((Birth)evt).requestNumber, time);
                    queue.AddLast(newRequest);

                    // if request is the only one, we must "start service" for that request
                    if (queue.Count == 1)
                    {
                        // mark service start time
                        newRequest.svcStartTime = time;
                        newRequest.status = RequestStatus.Processing;
                        // generate "service time"
                        double svcTime = getServiceTime();
                        newRequest.svcEndTime = time + svcTime;
                        // schedule death event
                        Death newDeath = new Death(newRequest.id, time + svcTime);
                        schedule.Enqueue(newDeath.scheduledTime, newDeath);
                    }
                }

                // schedule the next birth
                double interarrivalTime = this.nextExponentialDouble(cpu_Lambda, rand.NextDouble());
                Birth newBirth = new Birth(ticketNumber++, time + interarrivalTime);
                schedule.Enqueue(newBirth.scheduledTime, newBirth);
            }
            else if (evt is Death)
            {
                // remove the request from the queue
                Request toFind = new Request(((Death)evt).requestNumber, 0.0);
                Request finishedRequest = queue.Find(toFind).Value;
                queue.Remove(toFind);

                double responseTime = finishedRequest.svcEndTime - finishedRequest.creationTime;
                double serviceTime = finishedRequest.svcEndTime - finishedRequest.svcStartTime;
                double waitTime = responseTime - serviceTime;
                // collect its statistics (response time, wait time) [only in the 2nd half of the simulation]
                if (time >= requestedEndTime)
                {
                    totalResponseTime += responseTime;
                    totalWaitTime += waitTime;
                    requestsProcessed++;
                }

                // if there are requests in the queue, start the "next" requests
                int coresLeft = 1;
                if (sysType == SystemType.FULL)
                {
                    coresLeft = 2;
                }

                foreach (Request r in queue)
                {
                    if (r.status == RequestStatus.Waiting)
                    {
                        if (coresLeft > 0)
                        {
                            // mark service start time for next request
                            r.svcStartTime = time;
                            r.status = RequestStatus.Processing;

                            // generate "service time"
                            double svcTime = getServiceTime();
                            r.svcEndTime = time + svcTime;

                            // schedule its death event
                            Death newDeath = new Death(r.id, time + svcTime);
                            schedule.Enqueue(newDeath.scheduledTime, newDeath);

                            // use a core
                            coresLeft--;
                        }
                    }
                    else if (r.status == RequestStatus.Processing)
                    {
                        coresLeft--; // core already in use by another request
                    }
                }
            }
            else if (evt is Monitor)
            {
                // only calculate statistics in 2nd half of simulation
                if (time >= requestedEndTime)
                {
                    totalObservations++;

                    // update average queue length
                    totalInQueue += queue.Count;

                    // update average system occupancy [in queue + in service (i.e. count of deaths)]
                    int currInSystem = queue.Count;
                    foreach (KeyValuePair<double, Event> e in schedule)
                    {
                        if (e.Value is Death)
                        {
                            currInSystem++;
                        }
                    }
                    totalInSystem += currInSystem;

                    // store stuff into observation
                    ((Monitor)evt).w = queue.Count;
                    ((Monitor)evt).q = currInSystem;
                    ((Monitor)evt).Tq = averageResponseTime();
                    if (cpu_K > 0)
                    {
                        double rho = cpu_Lambda * cpu_Ts.mean;
                        ((Monitor)evt).probRejection = (1 - rho) * Math.Pow(rho, cpu_K) / (1 - Math.Pow(rho, cpu_K + 1));
                    }

                    // remember observation
                    observations.AddLast((Monitor)evt);

                    // print current time and statistics
                    Console.WriteLine("Step {0,5} | Time {1,8:0.0000} | Queue: {2,3} | Schedule: {3,3} | Reqs Proc.: {4,4} | Tw: {5:0.0000}s | w: {6:0.00} | Tq: {7:0.0000}s | q: {8:0.00}", step, time, queue.Count, schedule.Count, requestsProcessed, averageWaitTime(), averageInQueue(), averageResponseTime(), averageInSystem());

                    // write to file
                    if (exportQueueToFile)
                    {
                        using (System.IO.StreamWriter file = new System.IO.StreamWriter(@"D:\Dropbox\homework\college\4 senior\spring 11\cs350\hw4\CS350Simulator\CS350Simulator\bin\Debug\cpu.txt", true))
                        {
                            file.WriteLine("{0}\t{1}", time, queue.Count);
                        }
                    }
                }

                // schedule the next monitor event
                double interarrivalTime = this.nextExponentialDouble(1, rand.NextDouble());
                Monitor newMonitor = new Monitor(time + interarrivalTime);
                schedule.Enqueue(newMonitor.scheduledTime, newMonitor);
            }

            // increase step counter
            step++;

            return true;
        }

        private double nextExponentialDouble(double lambda, double uniform)
        {
            return (-1 * Math.Log(1 - uniform)) / lambda;
        }

        public double getServiceTime()
        {
            double svcTime = cpu_Ts.mean;

            switch (cpu_Ts.dist)
            {
                case ServiceDistribution.EXPONENTIAL:
                    svcTime = this.nextExponentialDouble(1 / cpu_Ts.mean, rand.NextDouble());
                    break;
                case ServiceDistribution.UNIFORM:
                    svcTime = rand.Next(10, 31);
                    svcTime /= 1000;
                    break;
                case ServiceDistribution.NORMAL:
                    MersenneTwister mt = new MersenneTwister();
                    svcTime = mt.Next(80, 120) / 1000;
                    break;
            }

            return svcTime;
        }

        public double averageResponseTime()
        {
            return totalResponseTime / requestsProcessed;
        }

        public double averageWaitTime()
        {
            return totalWaitTime / requestsProcessed;
        }

        public double averageInQueue()
        {
            return totalInQueue / totalObservations;
        }

        public double averageInSystem()
        {
            return totalInSystem / totalObservations;
        }

        public string confidenceInterval(double mean, double stdDev, double zScore, int samples)
        {
            double error = zScore * (stdDev / Math.Sqrt(samples));
            return String.Format("{0:0.00000}", mean) + " +/- " + String.Format("{0:0.00000}", error);
        }

        public double calcMean(LinkedList<double> list)
        {
            double sum = 0;
            foreach (double d in list)
            {
                sum += d;
            }
            return sum / list.Count;
        }

        public double calcStdDev(double mean, LinkedList<double> list)
        {
            double sumVar = 0;
            foreach (double d in list)
            {
                sumVar += Math.Pow(d - mean, 2);
            }
            return Math.Sqrt(sumVar / list.Count);
        }

        public string ConfInt95th(string stat)
        {
            LinkedList<double> list = new LinkedList<double>();
            foreach (Monitor m in observations)
            {
                switch (stat)
                {
                    case "q":
                        list.AddLast(m.q);
                        break;
                    case "Tq":
                        list.AddLast(m.Tq);
                        break;
                    case "reject":
                        list.AddLast(m.probRejection);
                        break;
                    case "w":
                        list.AddLast(m.w);
                        break;
                }
            }
            double mean = calcMean(list);
            double stdDev = calcStdDev(mean, list);
            return confidenceInterval(mean, stdDev, ZSCORE_95TH, observations.Count);
        }

        public string ConfInt98th(string stat)
        {
            LinkedList<double> list = new LinkedList<double>();
            foreach (Monitor m in observations)
            {
                switch (stat)
                {
                    case "q":
                        list.AddLast(m.q);
                        break;
                    case "Tq":
                        list.AddLast(m.Tq);
                        break;
                    case "reject":
                        list.AddLast(m.probRejection);
                        break;
                    case "w":
                        list.AddLast(m.w);
                        break;
                }
            }
            double mean = calcMean(list);
            double stdDev = calcStdDev(mean, list);
            return confidenceInterval(mean, stdDev, ZSCORE_98TH, observations.Count);
        }

    }

}
