using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Diagnostics;

namespace CS350Simulator
{

    class Controller
    {
        static void Main(string[] args)
        {
            // ask user for simulation parameters
            Console.WriteLine("Welcome to Matthew Huynh's Round-Robin Scheduler Simulator!\n");
            
            int cpu_lambda = 30;
            double simulationTime = 100.0;
            ServiceTime cpu_Ts = new ServiceTime();
            cpu_Ts.dist = ServiceDistribution.EXPONENTIAL;
            cpu_Ts.mean = 0.03; // seconds
            int cpuQueueSize = 0;

            // INDEPENDENT VARIABLE
            double quantum = 0.001; // seconds

            // initialize simulator
            Simulator cpuSim = new Simulator(cpu_lambda, simulationTime, cpu_Ts, cpuQueueSize, quantum);

            // turn on selection of shortest request
            cpuSim.selectShortestReq = true;

            Console.WriteLine("lambda = {0}, Ts = {1}, c = {2}s, simulation time = {3}, seed = {4}\n", cpu_lambda, cpu_Ts.ToString(), quantum, simulationTime, cpuSim.seed);

            Console.WriteLine("Simulation started.\n--------------------");
            while (cpuSim.Step())
            {
            } // run the simulation to the end
            Console.WriteLine("--------------------\nSimulation finished.\n");

            // write slowdown stats to file
            cpuSim.writeSlowdownStatsToFile(@"D:\Dropbox\homework\college\4 senior\spring 11\cs350\hw5\slowdown.txt");

            double q = 0;
            double Tq = 0;
            //double Tw = 0;
            //double w = 0;
            //double probRejection = 0;

            // calculate stats
            double rho = cpu_lambda * quantum / (quantum / cpu_Ts.mean);
            q = rho / (1 - rho);
            Tq = q / cpu_lambda;
            //Tw = Tq - cpu_Ts.mean;
            //w = cpu_lambda * Tw;

            // print stats (if not overloaded)
            if (rho <= 1)
            {
                Console.WriteLine("expected statistics");
                Console.WriteLine("rho: {0:0.00}", rho);
                Console.WriteLine("Tq: {0:0.0000}s", Tq);
                Console.WriteLine("q : {0:0.0000} people", q);
            }
            else
            {
                Console.WriteLine("System overloaded at {0:0.00}% utilization!\n", rho * 100);
            }

            Console.WriteLine("\nempirical statistics");
            Console.WriteLine("Tq: {0:0.0000}s", cpuSim.averageResponseTime());
            Console.WriteLine("q: {0:0.000} people", cpuSim.averageInSystem());
            Console.WriteLine("slowdown: {0:0.000}", cpuSim.averageSlowdown());

            Console.ReadKey(); // pause at end of program
        }

        private static int promptQueueSize()
        {
            Console.Write("K?: ");
            return Int32.Parse(Console.ReadLine());
        }

        private static double promptMean()
        {
            Console.Write("Ts?: ");
            return Double.Parse(Console.ReadLine());
        }

        private static int promptLambda()
        {
            Console.Write("Lambda?: ");
            return Int32.Parse(Console.ReadLine());
        }

        private static double promptSimTime()
        {
            Console.Write("Simulation time?: ");
            return Double.Parse(Console.ReadLine());
        }
    }
}
