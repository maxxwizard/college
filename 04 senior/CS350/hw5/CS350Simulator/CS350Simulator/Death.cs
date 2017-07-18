using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CS350Simulator
{
    class Death : Event
    {
        public int requestNumber;
      
        public Death(int requestNumber, double currentTime) : base(currentTime)
        {
            this.requestNumber = requestNumber;
        }
    }
}
