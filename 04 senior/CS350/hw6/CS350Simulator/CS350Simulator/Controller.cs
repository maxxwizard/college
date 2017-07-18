using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Diagnostics;

namespace CS350Simulator
{
    public enum SystemType { RANDOM, FCFS, SCAN };

    class Controller
    {
        static void Main(string[] args)
        {
            Console.WriteLine("Welcome to Matthew Huynh's Disk Scheduling Simulator!\n");

            // ask user for simulation parameters
            Console.WriteLine("1. RANDOM");
            Console.WriteLine("2. FCFS");
            Console.WriteLine("3. SCAN");
            Console.Write("\nWhich scheduler to simulate? ");
            int choice = Int32.Parse(Console.ReadLine());

            SystemType sysType = SystemType.RANDOM;
            double workload = 10000; // requests
            double lambda = 50; // per second
            int N = 500; // tracks
            double U = 0.002; // seconds
            double V = 0.0001; // seconds/track

            // set scheduler based on user input
            switch (choice)
            {
                case 1:
                    sysType = SystemType.RANDOM;
                    break;
                case 2:
                    sysType = SystemType.FCFS;
                    break;
                case 3:
                    sysType = SystemType.SCAN;
                    break;
            }
            
            // initialize simulator
            Simulator sim = new Simulator(sysType, workload, lambda, N, U, V);
            
            Console.WriteLine("\nSimulator initialized with following parameters:");
            Console.WriteLine("scheduler = {0}\nlambda = {1} requests/s\nN = {2} tracks\nU = {3}ms\nV = {4}ms/track\nworkload = {5} requests\nrandom seed = {6}\n", sysType, lambda, N, U*1000, V*1000, workload, sim.seed);

            Console.WriteLine("Simulation started.");
            while (sim.Step())
            {
            } // run the simulation to the end
            Console.WriteLine("Simulation finished.\n");

            // calculate and print out empirical stats
            sim.CalculateEmpiricalStats();

            Console.ReadKey(); // pause at end of program
        }
    }
}
