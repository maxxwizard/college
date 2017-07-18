using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CS350Simulator
{
    class Request
    {
        public int id;
        public double arrivalTime;
        public double reqSvcTime;
        public double svcTimeRemaining;

        public Request(int id, double creationTime, double reqServiceTime)
        {
            this.id = id;
            this.arrivalTime = creationTime;
            this.reqSvcTime = reqServiceTime;
            this.svcTimeRemaining = reqServiceTime;
        }

        public static bool operator ==(Request r1, Request r2)
        {
            return r1.id == r2.id;
        }

        public static bool operator !=(Request r1, Request r2)
        {
            return r1.id != r2.id;
        }

        public override bool Equals(object obj)
        {
            return this == (Request)obj;
        }

        public override int GetHashCode()
        {
            return this.id;
        }
    }
}
