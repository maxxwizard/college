using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace CS350Simulator
{
    enum Direction { UP, DOWN };
    class Simulator
    {
        #region SIMULATION SETTINGS
        public SystemType sysType;
        double workload, lambda, U, V;
        int N;
        public int seed;
        Random rand;
        public bool exportHeadMovementToFile = false;
        public bool exportResponseTimeToFile = false;
        #endregion

        #region SIMULATION VARIABLES
        public int step;
        double time;
        int currentTrack; // where the disk head is currently at
        Direction direction = Direction.UP; // the disk starts at the "bottom" and moves up
        public PriorityQueue<double, Event> schedule;
        public LinkedList<Request> queue;
        public LinkedList<double> responseTimes;
        public int ticketNumber = 0;
        public int requestsProcessed = 0;
        public double totalHeadMovement = 0;
        public double totalServiceTime = 0;
        public double totalResponseTime = 0;
        #endregion

        #region CONSTANTS
        const double ZSCORE_95TH = 1.96;
        #endregion

        public Simulator(SystemType sysType, double workload, double lambda, int N, double U, double V)
        {
            // set simulation time to 0
            this.time = 0;
            this.step = 0;

            // set simulation settings
            this.sysType = sysType;
            this.workload = workload;
            this.lambda = lambda;
            this.N = N;
            this.U = U;
            this.V = V;
            this.currentTrack = 1;

            // initialize variables
            this.schedule = new PriorityQueue<double, Event>();
            this.queue = new LinkedList<Request>();
            this.responseTimes = new LinkedList<double>();

            // make a new Random number generator and remember the seed
            seed = (int)DateTime.Now.Ticks;
            rand = new Random(seed);

            // initialize schedule by adding a single birth
            schedule.Enqueue(0.0, new Birth(ticketNumber++, 0.0));
        }

        /// <summary>
        /// Performs a step of the simulation and increases the step counter by 1.
        /// </summary>
        /// <returns>A boolean which represents whether the simulation is over or not.</returns>
        public bool Step()
        {
            // guard against stepping past simulation end
            if (this.schedule.Count <= 0)
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
                Request newRequest = new Request(((Birth)evt).requestNumber, time, getRandomTrack());
                queue.AddLast(newRequest);

                // if request is the only one, we must "start service" for that request
                if (queue.Count == 1)
                {
                    ServiceNextRequest();
                }

                // schedule the next birth (if ticketNumber < workload)
                if (ticketNumber < workload)
                {
                    double interarrivalTime = this.nextExponentialDouble(lambda, rand.NextDouble());
                    Birth newBirth = new Birth(ticketNumber++, time + interarrivalTime);
                    schedule.Enqueue(newBirth.scheduledTime, newBirth);
                }
            }
            else if (evt is Death)
            {
                // remove the request from the queue
                Request toFind = new Request(((Death)evt).requestNumber, 0.0, 0);
                Request finishedRequest = queue.Find(toFind).Value;
                queue.Remove(toFind);

                // collect its statistics (head movement, response time)
                double responseTime = finishedRequest.svcEndTime - finishedRequest.creationTime;
                double serviceTime = finishedRequest.svcEndTime - finishedRequest.svcStartTime;
                double waitTime = responseTime - serviceTime;
                int headMovement = Math.Abs(currentTrack - finishedRequest.requestedTrack);
                currentTrack = finishedRequest.requestedTrack; // move the track head (since we just serviced it)
                totalHeadMovement += headMovement;
                totalServiceTime += serviceTime;
                totalResponseTime += responseTime;
                responseTimes.AddLast(responseTime);
                requestsProcessed++;

                //Console.WriteLine("queue length: {0}", queue.Count);

                // if there are requests in the queue, start the "next" request
                if (queue.Count > 0)
                {
                    ServiceNextRequest();
                }
            }

            // increase step counter
            step++;

            return true;
        }

        private void ServiceNextRequest()
        {
            Request nextRequest = queue.First.Value; // default to FCFS
            switch (sysType)
            { // choose next request differently if RANDOM or SCAN scheduler requested
                case SystemType.RANDOM:
                    int nextRequestIndex = rand.Next(0, queue.Count - 1);
                    nextRequest = queue.ElementAt(nextRequestIndex);
                    break;
                case SystemType.SCAN:
                    int nextRequestID = 0;
                    switch (direction)
                    {
                        case Direction.UP:
                            // find next minimum higher-tracked request
                            nextRequestID = NextHigherRequest();
                            // if none, flip direction and find next lower-tracked request
                            if (nextRequestID == 0)
                            {
                                direction = Direction.DOWN;
                                nextRequestID = NextLowerRequest();
                            }
                            break;
                        case Direction.DOWN:
                            // find next maximum lower-tracked request
                            nextRequestID = NextLowerRequest();
                            // if none, flip direction and find next higher-tracked request
                            if (nextRequestID == 0)
                            {
                                direction = Direction.UP;
                                nextRequestID = NextHigherRequest();
                            }
                            break;
                    }
                    nextRequest = queue.Find(new Request(nextRequestID, 0.0, 0)).Value;
                    break;
            }
            
            // perform service
            nextRequest.svcStartTime = time;
            nextRequest.svcEndTime = time + getServiceTime(nextRequest.requestedTrack);

            // schedule its death
            Death newDeath = new Death(nextRequest.id, nextRequest.svcEndTime);
            schedule.Enqueue(newDeath.scheduledTime, newDeath);
        }

        /// <summary>
        /// Returns ID of the next minimum higher-tracked request, else 0 if none found.
        /// </summary>
        private int NextHigherRequest()
        {
            int nextRequest = 0;
            int minTrack = N;
            foreach (Request r in queue)
            {
                if (r.requestedTrack >= currentTrack && r.requestedTrack <= minTrack)
                {
                    nextRequest = r.id;
                }
            }
            return nextRequest;
        }

        /// <summary>
        /// Returns ID of the next maximum lower-tracked request, else 0 if none found.
        /// </summary>
        private int NextLowerRequest()
        {
            int nextRequest = 0;
            int maxTrack = 0;
            foreach (Request r in queue)
            {
                if (r.requestedTrack <= currentTrack && r.requestedTrack >= maxTrack)
                {
                    nextRequest = r.id;
                }
            }
            return nextRequest;
        }

        /// <summary>
        /// Returns a track number from a uniform distribution.
        /// </summary>
        public int getRandomTrack()
        {
            return rand.Next(1, N);
        }

        /// <summary>
        /// Service time = U + V * |y-x|, where y is the track head's prior location and x is the requested location
        /// </summary>
        /// <returns>The service time to move from the current track to the requested one.</returns>
        public double getServiceTime(int previousTrack)
        {
            return U + V * (previousTrack - currentTrack);
        }

        public double averageServiceTime()
        {
            return totalServiceTime / requestsProcessed;
        }

        public double averageResponseTime()
        {
            return totalResponseTime / requestsProcessed;
        }

        public double averageHeadMovement()
        {
            return totalHeadMovement / requestsProcessed;
        }
        
        /// <summary>
        /// http://stackoverflow.com/questions/2253874/linq-equivalent-for-standard-deviation
        /// </summary>
        public double calcStdDev(double mean, LinkedList<double> list)
        {
            double ret = 0;

            if (list.Count() > 1)
            {
                //Perform the Sum of (value-avg)^2
                double sum = list.Sum(d => (d - mean) * (d - mean));

                //Put it all together
                ret = Math.Sqrt((sum) / (double)list.Count());
            }

            return ret;
        }

        public string ConfInt95th()
        {
            double mean = responseTimes.Average();
            double stdDev = calcStdDev(mean, responseTimes);
            double error = ZSCORE_95TH * (stdDev / Math.Sqrt(responseTimes.Count));
            return String.Format("{0}ms", mean*1000) + " +/- " + String.Format("{0}ms", error*1000);
        }

        /*
        /// <summary>
        /// Calculates expected stats (M/M/1 approx) of the simulation and prints it out.
        /// </summary>
        public void CalculateExpectedStats()
        {
            double avgHeadMovement = 0;
            double totalHeadMovement = 0;
            double avgServiceTime = 0;
            double avgResponseTime = 0;
            double rho = 0;
            Console.WriteLine("Expected statistics:");
            switch (sysType)
            {
                case SystemType.RANDOM:
                    // avgHeadMovement = E[Y-X] = E[Y] - E[X] = ((N+0.5N)/2) - 0.5N = 0.75N - 0.5N = 0.25N
                    avgHeadMovement = 0.25*N;
                    avgServiceTime = U + V * avgHeadMovement;
                    rho = lambda * avgServiceTime;
                    avgResponseTime = 1 / ((1 / avgServiceTime) * (1 - rho));
                    totalHeadMovement = avgHeadMovement * workload;
                    break;
            }
            Console.WriteLine("Total head movement = {0} tracks\nAvg head movement = {1} tracks\nAvg service time = {2}ms\nAvg response time = {3}ms", totalHeadMovement, avgHeadMovement, avgServiceTime*1000, avgResponseTime*1000);
        }
         */

        /// <summary>
        /// Calculates empirical stats of the simulation and prints it out.
        /// </summary>
        public void CalculateEmpiricalStats()
        {
            Console.WriteLine("Empirical stats:");
            Console.WriteLine("Total head movement = {0} tracks", totalHeadMovement);
            Console.WriteLine("Avg head movement = {0} tracks", averageHeadMovement());
            Console.WriteLine("Avg service time = {0}ms", averageServiceTime()*1000);
            Console.WriteLine("Avg response time = {0}ms", averageResponseTime()*1000);
            Console.WriteLine("95th CI for response time = {0}", ConfInt95th());

            if (exportHeadMovementToFile)
            {
                using (System.IO.StreamWriter file = new System.IO.StreamWriter(@"D:\Dropbox\homework\college\4 senior\spring 11\cs350\hw6\CS350Simulator\CS350Simulator\bin\Debug\headMovement.txt", true))
                {
                    file.WriteLine("{0}", totalHeadMovement); // tracks
                }
            }

            if (exportResponseTimeToFile)
            {
                using (System.IO.StreamWriter file = new System.IO.StreamWriter(@"D:\Dropbox\homework\college\4 senior\spring 11\cs350\hw6\CS350Simulator\CS350Simulator\bin\Debug\responseTime.txt", true))
                {
                    file.WriteLine("{0}", averageResponseTime()*1000); // milliseconds
                }
            }
        }

        private double nextExponentialDouble(double lambda, double uniform)
        {
            return (-1 * Math.Log(1 - uniform)) / lambda;
        }

    }

}
