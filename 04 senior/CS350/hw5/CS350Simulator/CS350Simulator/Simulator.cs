using System;
using System.Collections;
using System.Linq;
using System.Text;
using System.IO;
using System.Collections.Generic;

namespace CS350Simulator
{
    class Simulator
    {
        #region SIMULATION SETTINGS
        public int seed;
        double requestedEndTime;
        double simEndTime;
        public int step;
        public double time;
        Random rand;
        #endregion

        #region CPU SETTINGS
        double cpu_Lambda;
        ServiceTime cpu_Ts;
        int cpu_K = 0; // 0 = infinite queue
        double timeQuantum;
        public bool selectShortestReq = false;
        #endregion

        #region CPU VARIABLES
        public PriorityQueue<double, Event> schedule;
        public LinkedList<Request> queue;
        public int ticketNumber = 0;
        public int requestsProcessed = 0;
        public double totalResponseTime = 0;
        //public int totalInQueue = 0;
        public int totalInSystem = 0;
        //public int rejectCount = 0;
        //public int totalObservations = 0;
        //public LinkedList<Monitor> observations;
        /// <summary>
        /// Each Tuple is (bucket number, slowdown).
        /// </summary>
        public List<Tuple<int, double>> slowdownBuckets;
        #endregion

        #region CONSTANTS
        const double ZSCORE_95TH = 1.96;
        const double ZSCORE_98TH = 2.33;
        const double ZSCORE_99TH = 2.58;
        #endregion

        public Simulator(double lambda, double totalTime, ServiceTime Ts, int queueSize, double quantum)
        {
            // set simulation time to 0
            this.time = 0;

            // set simulation settings
            this.cpu_Lambda = lambda;
            this.cpu_Ts = Ts;
            this.requestedEndTime = totalTime;
            this.simEndTime = 2 * totalTime;
            this.cpu_K = queueSize;
            this.timeQuantum = quantum;

            // initialize variables
            this.schedule = new PriorityQueue<double, Event>();
            this.queue = new LinkedList<Request>();
            //this.observations = new LinkedList<Monitor>();

            // make a new Random number generator and remember the seed
            seed = (int)DateTime.Now.Ticks;
            rand = new Random(seed);

            // initialize schedule by adding a single birth
            schedule.Enqueue(0.0, new Birth(ticketNumber++, 0.0));

            // also add a monitor event (interarrival time is exponentially distributed with mean of 1s)
            double interarrivalTime = this.nextExponentialDouble(1, rand.NextDouble());
            schedule.Enqueue(0.0, new Monitor(interarrivalTime));

            // initialize slowdown buckets
            slowdownBuckets = new List<Tuple<int,double>>();
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
                // add request to request queue
                Request newRequest = new Request(((Birth)evt).requestNumber, time, getServiceTime());
                queue.AddLast(newRequest);

                if (selectShortestReq)
                {
                    // perform 1 quantum of service on shortest request
                    ServiceRequest(NextShortestJob());
                }
                else
                {
                    // perform 1 quantum of service on new request
                    ServiceRequest(newRequest.id);
                }

                // schedule the next birth
                double interarrivalTime = this.nextExponentialDouble(cpu_Lambda, rand.NextDouble());
                Birth newBirth = new Birth(ticketNumber++, time + interarrivalTime);
                schedule.Enqueue(newBirth.scheduledTime, newBirth);
            }
            else if (evt is Timeout)
            {
                if (selectShortestReq)
                {
                    ServiceRequest(NextShortestJob());
                }
                else
                {
                    // perform 1 quantum of service on this request
                    ServiceRequest(((Timeout)evt).requestNumber);
                }
            }
            else if (evt is Death)
            {
                // remove the request from the queue
                Request toFind = new Request(((Death)evt).requestNumber, 0.0, 0.0);
                Request finishedRequest = queue.Find(toFind).Value;
                queue.Remove(toFind);

                // collect its statistics (response time, slowdown) [only in the 2nd half of the simulation]
                if (time >= requestedEndTime)
                {
                    double responseTime = time - finishedRequest.arrivalTime;
                    double serviceTime = finishedRequest.reqSvcTime;
                    double slowdown = responseTime / serviceTime;

                    totalInSystem += queue.Count;
                    totalResponseTime += responseTime;
                    requestsProcessed++;

                    saveIntoSlowdownBucket(finishedRequest.reqSvcTime, slowdown);
                }
            }
            /*
        else if (evt is Monitor)
        {
            // only calculate statistics in 2nd half of simulation
            if (time >= requestedEndTime)
            {
                totalObservations++;

                // update average system occupancy
                totalInSystem += queue.Count;

                // store stuff into observation
                ((Monitor)evt).q = queue.Count;
                ((Monitor)evt).Tq = averageResponseTime();
                if (cpu_K > 0)
                {
                    double rho = cpu_Lambda * cpu_Ts.mean;
                    ((Monitor)evt).probRejection = (1 - rho) * Math.Pow(rho, cpu_K) / (1 - Math.Pow(rho, cpu_K + 1));
                }

                // remember observation
                observations.AddLast((Monitor)evt);

                // print current time and statistics
                Console.WriteLine("Step {0,5} | Time {1,8:0.0000} | Queue: {2,3} | Schedule: {3,3} | Reqs Proc.: {4,4} | Tq: {5:0.0000}s | q: {6:0.00}", step, time, queue.Count, schedule.Count, requestsProcessed, averageResponseTime(), averageInSystem());

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
        */

            // increase step counter
            step++;

            return true;
        }

        private void ServiceRequest(int requestID)
        {
            foreach (Request r in queue) // find request
            {
                if (r.id == requestID)
                {
                    // perform 1 quantum of service
                    r.svcTimeRemaining -= timeQuantum;
                    
                    // schedule its timeout or death
                    if (r.svcTimeRemaining <= 0)
                    { // death
                        Death newDeath = new Death(r.id, time + timeQuantum);
                        schedule.Enqueue(newDeath.scheduledTime, newDeath);
                    }
                    else
                    { // timeout
                        // if 'select the shortest job' is turned on, queue up the job with least time remaining
                        if (selectShortestReq)
                        {
                            int smallest = NextShortestJob();
                            Timeout newTimeout = new Timeout(smallest, time + timeQuantum);
                            schedule.Enqueue(newTimeout.scheduledTime, newTimeout);
                        }
                        else
                        {
                            // send the request to the 'back' of the queue
                            Timeout newTimeout = new Timeout(r.id, time + queue.Count * timeQuantum);
                            schedule.Enqueue(newTimeout.scheduledTime, newTimeout);
                        }
                    }
                }
            }
        }

        /// <summary>
        /// Returns the request id of the job with the least service time remaining.
        /// </summary>
        /// <returns></returns>
        private int NextShortestJob()
        {
            Request smallest = new Request(0, 0.0, 99999);
            foreach (Request r in queue)
            {
                if (r.svcTimeRemaining < smallest.svcTimeRemaining && r.svcTimeRemaining > 0)
                {
                    smallest = r;
                }
            }
            return smallest.id;
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
            }

            return svcTime;
        }

        public double averageResponseTime()
        {
            return totalResponseTime / requestsProcessed;
        }

        public double averageInSystem()
        {
            return (double)totalInSystem / (double)requestsProcessed;
        }

        public double averageSlowdown()
        {
            return slowdownBuckets.Average(t => t.Item2);
        }

        public void saveIntoSlowdownBucket(double reqSvcTime, double slowdown)
        {
            if (reqSvcTime < 0.01)
            {
                slowdownBuckets.Add(new Tuple<int, double>(0, slowdown));
            }
            else if (reqSvcTime < 0.02)
            {
                slowdownBuckets.Add(new Tuple<int, double>(1, slowdown));
            }
            else if (reqSvcTime < 0.03)
            {
                slowdownBuckets.Add(new Tuple<int, double>(2, slowdown));
            }
            else if (reqSvcTime < 0.04)
            {
                slowdownBuckets.Add(new Tuple<int, double>(3, slowdown));
            }
            else if (reqSvcTime < 0.05)
            {
                slowdownBuckets.Add(new Tuple<int, double>(4, slowdown));
            }
            else if (reqSvcTime < 0.06)
            {
                slowdownBuckets.Add(new Tuple<int, double>(5, slowdown));
            }
            else
            {
                slowdownBuckets.Add(new Tuple<int, double>(6, slowdown));
            }
        }

        public void writeSlowdownStatsToFile(string pathToFile)
        {
            // write slowdown stats to file
            using (System.IO.StreamWriter file = new System.IO.StreamWriter(pathToFile, true))
            {
                var averages = from t in slowdownBuckets
                                       group t by t.Item1 into g
                                       orderby g.Key
                                       select new { Bucket = g.Key, Average = g.Average(i => i.Item2) };
                foreach (var item in averages)
                {
                    file.WriteLine("{0}", item.Average);      
                }
            }
        }
    }

}
