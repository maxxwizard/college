using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CS350Simulator
{
    class Request
    {
        public int id;
        public double creationTime;
        public double svcStartTime;
        public double svcEndTime;
        public int requestedTrack;

        public Request(int id, double creationTime, int requestedTrack)
        {
            this.id = id;
            this.creationTime = creationTime;
            this.requestedTrack = requestedTrack;
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
