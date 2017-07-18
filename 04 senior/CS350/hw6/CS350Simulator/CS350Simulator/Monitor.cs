using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CS350Simulator
{
    class Monitor : Event
    {
        public double w;
        public double q;
        public double Tq;
        public double probRejection;

        public Monitor(double arrivalTime) : base(arrivalTime)
        {
        }
    }
}
