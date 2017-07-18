using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CS350Simulator
{
    public enum ServiceDistribution { FIXED, EXPONENTIAL, UNIFORM, NORMAL };
    public struct ServiceTime
    {
        public ServiceDistribution dist;
        public double mean;
        public double stdDev;
        public double uniform_min;
        public double uniform_max;

        public override string ToString()
        {
            string s = "";
            switch (dist)
            {
                case ServiceDistribution.FIXED:
                    s = "fixed " + mean;
                    break;
                case ServiceDistribution.EXPONENTIAL:
                    s = "expon " + mean;
                    break;
                case ServiceDistribution.UNIFORM:
                    s = "uni (" + uniform_min + ", " + uniform_max + ")";
                    break;
                case ServiceDistribution.NORMAL:
                    s = "norm (" + mean + ", " + stdDev + ")";
                    break;
            }
            return s;
        }
    };
}
