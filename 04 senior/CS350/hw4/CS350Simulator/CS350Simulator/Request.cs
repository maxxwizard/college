using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CS350Simulator
{
    public enum RequestStatus { Waiting, Processing };

    class Request
    {
        public RequestStatus status;
        public int id;
        public double creationTime;
        public double svcStartTime;
        public double svcEndTime;

        public Request(int id, double creationTime)
        {
            status = RequestStatus.Waiting;
            this.id = id;
            this.creationTime = creationTime;
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
