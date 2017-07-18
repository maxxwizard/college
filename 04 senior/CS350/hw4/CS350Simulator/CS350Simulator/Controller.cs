using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Diagnostics;

namespace CS350Simulator
{
    public enum SystemType { MM1, MM1K, MD1K, FULL };

    class Controller
    {
        static void Main(string[] args)
        {
            // ask user for simulation parameters
            Console.WriteLine("Welcome to Matthew Huynh's Discrete Event Simulator!\n");
            
            Console.WriteLine("1. M/M/1");
            Console.WriteLine("2. M/M/1/K");
            Console.WriteLine("3. M/D/1/K");
            Console.WriteLine("4. M/U/2 CPU, M/N/1 Disk, M/D/1 Network");
            Console.Write("\nWhich system to simulate? ");
            int choice = Int32.Parse(Console.ReadLine());

            SystemType sysType = SystemType.MM1;
            int cpu_lambda = promptLambda();
            double simulationTime = promptSimTime();
            ServiceTime cpu_Ts = new ServiceTime();
            int cpuQueueSize = 0;

            switch (choice)
            {
                case 1:
                    sysType = SystemType.MM1;
                    cpu_Ts.dist = ServiceDistribution.EXPONENTIAL;
                    cpu_Ts.mean = promptMean();
                    break;
                case 2:
                    sysType = SystemType.MM1K;
                    cpu_Ts.dist = ServiceDistribution.EXPONENTIAL;
                    cpuQueueSize = promptQueueSize();
                    break;
                case 3:
                    sysType = SystemType.MD1K;
                    cpu_Ts.dist = ServiceDistribution.FIXED;
                    cpuQueueSize = promptQueueSize();
                    break;
                case 4:
                    sysType = SystemType.FULL;

                    cpu_Ts.dist = ServiceDistribution.UNIFORM;
                    cpu_Ts.uniform_min = 0.010;
                    cpu_Ts.uniform_max = 0.030;

                    
                    break;
            }
            
            // initialize simulators
            Simulator cpuSim = new Simulator(sysType, cpu_lambda*2, simulationTime, cpu_Ts, cpuQueueSize);
            cpuSim.exportQueueToFile = true;

            ServiceTime disk_Ts = new ServiceTime();
            disk_Ts.dist = ServiceDistribution.FIXED;
            disk_Ts.mean = .110;
            Simulator diskSim = new Simulator(SystemType.MM1, 0.1*cpu_lambda, simulationTime, disk_Ts, 0);

            ServiceTime net_Ts = new ServiceTime();
            net_Ts.dist = ServiceDistribution.FIXED;
            net_Ts.mean = 0.025;
            Simulator netSim = new Simulator(SystemType.MD1K, 0.9*cpu_lambda, simulationTime, net_Ts, 0);

            Console.WriteLine("\n{0}, lambda = {1}, Ts {2}, simulation time = {3}, seed = {4}\n", sysType, cpu_lambda, cpu_Ts.ToString(), simulationTime, cpuSim.seed);

            Console.WriteLine("Simulation started.\n--------------------");
            while (cpuSim.Step())
            {
                if (sysType == SystemType.FULL)
                {
                    diskSim.Step();
                    netSim.Step();
                }
            } // run the simulation to the end
            Console.WriteLine("--------------------\nSimulation finished.\n");

            double rho = cpu_lambda * cpu_Ts.mean;
            if (rho > 1)
                Console.WriteLine("System overloaded at {0:0.00}% utilization!\n", rho * 100);

            double q = 0;
            double Tq = 0;
            double Tw = 0;
            double w = 0;
            double probRejection = 0;
            switch (sysType)
            {
                case SystemType.MM1:
                    // calculate stats
                    q = rho / (1 - rho);
                    Tq = q / cpu_lambda;
                    Tw = Tq - cpu_Ts.mean;
                    w = cpu_lambda * Tw;

                    // print stats (if not overloaded)
                    if (rho <= 1)
                    {
                        Console.WriteLine("{0} expected statistics", sysType);
                        Console.WriteLine("Tw: {0:0.0000}s", Tw);
                        Console.WriteLine("w : {0:0.0000} people", w);
                        Console.WriteLine("Tq: {0:0.0000}s", Tq);
                        Console.WriteLine("q : {0:0.0000} people", q);
                    }
                    break;
                case SystemType.MM1K:
                    // calculate stats
                    if (rho != 1)
                    {
                        q = rho / (1 - rho);
                        q -= (cpuQueueSize + 1) * Math.Pow(rho, cpuQueueSize + 1) / (1 - Math.Pow(rho, cpuQueueSize + 1));
                        probRejection = (1 - rho) * Math.Pow(rho, cpuQueueSize) / (1 - Math.Pow(rho, cpuQueueSize + 1));
                    }
                    else
                    {
                        q = cpuQueueSize / 2;
                        probRejection = 1 / (cpuQueueSize + 1);
                    }
                    Tq = q / cpu_lambda;
                    Tw = Tq - cpu_Ts.mean;
                    w = cpu_lambda * Tw;

                    // print stats (if not overloaded)
                    if (rho <= 1)
                    {
                        Console.WriteLine("{0} expected statistics", sysType);
                        Console.WriteLine("Tw: {0:0.0000}s", Tw);
                        Console.WriteLine("w : {0:0.0000} people", w);
                        Console.WriteLine("Tq: {0:0.0000}s", Tq);
                        Console.WriteLine("q : {0:0.0000} people", q);
                    }
                    break;
            }

            Console.WriteLine("\n{0} empirical statistics", sysType);
            switch (sysType)
            {
                case SystemType.MM1:
                    Console.WriteLine("Tw: {0:0.0000}s", cpuSim.averageWaitTime());
                    Console.WriteLine("w : {0:0.00} people", cpuSim.averageInQueue());
                    Console.WriteLine("Tq: {0:0.0000}s", cpuSim.averageResponseTime());
                    Console.WriteLine("q : {0:0.00} people\n", cpuSim.averageInSystem());
                    break;
                case SystemType.MM1K:
                    Console.WriteLine("Tw: {0:0.0000}s", cpuSim.averageWaitTime());
                    Console.WriteLine("w : {0:0.00} people", cpuSim.averageInQueue());
                    Console.WriteLine("q's 95th CI: {0}", cpuSim.ConfInt95th("q"));
                    Console.WriteLine("Tq's 95th CI: {0}", cpuSim.ConfInt95th("Tq"));
                    Console.WriteLine("Pr[rejection]: {0}", cpuSim.ConfInt95th("reject"));
                    break;
                case SystemType.FULL:
                    Console.WriteLine("CPU");
                    Console.WriteLine("Tw: {0:0.0000}s", cpuSim.averageWaitTime());
                    Console.WriteLine("w : {0} people", cpuSim.averageInQueue());
                    Console.WriteLine("Tq: {0:0.0000}s", cpuSim.averageResponseTime());
                    Console.WriteLine("q : {0} people\n", cpuSim.averageInSystem());
                    Console.WriteLine("w 95th CI: {0} people\n", cpuSim.ConfInt95th("w"));
                    Console.WriteLine("w 98th CI: {0} people\n", cpuSim.ConfInt98th("w"));
                    Console.WriteLine("Tq 95th CI: {0} people\n", cpuSim.ConfInt95th("Tq"));
                    Console.WriteLine("Tq 98th CI: {0} people\n", cpuSim.ConfInt98th("Tq"));

                    Console.WriteLine("Disk");
                    Console.WriteLine("Tw: {0}s", diskSim.averageWaitTime());
                    Console.WriteLine("w : {0} people", diskSim.averageInQueue());
                    Console.WriteLine("Tq: {0}s", diskSim.averageResponseTime());
                    Console.WriteLine("q : {0} people\n", diskSim.averageInSystem());

                    Console.WriteLine("Net");
                    Console.WriteLine("Tw: {0}s", netSim.averageWaitTime());
                    Console.WriteLine("w : {0} people", netSim.averageInQueue());
                    Console.WriteLine("Tq: {0}s", netSim.averageResponseTime());
                    Console.WriteLine("q : {0} people\n", netSim.averageInSystem());
                    Console.WriteLine("total failed requests: {0}", netSim.rejectCount);

                    Console.WriteLine("Total");
                    Console.WriteLine("q : {0:0.00} people", cpuSim.averageInSystem()+diskSim.averageInSystem()+netSim.averageInSystem());
                    Console.WriteLine("Tq : {0:0.00}s\n", cpuSim.averageResponseTime() + diskSim.averageResponseTime() + netSim.averageResponseTime());
                    break;
            }

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
