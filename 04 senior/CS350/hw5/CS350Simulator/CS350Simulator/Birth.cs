using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CS350Simulator
{
    class Birth : Event
    {
        public int requestNumber;

        public Birth(int requestNumber, double currentTime) : base(currentTime)
        {
            this.requestNumber = requestNumber;
        }
    }
}
