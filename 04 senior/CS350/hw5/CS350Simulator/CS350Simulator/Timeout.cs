using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CS350Simulator
{
    class Timeout : Event
    {
        public int requestNumber;

        public Timeout(int requestNumber, double currentTime)
            : base(currentTime)
        {
            this.requestNumber = requestNumber;
        }
    }
}
