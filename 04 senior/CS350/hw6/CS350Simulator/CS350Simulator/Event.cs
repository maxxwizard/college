using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CS350Simulator
{
    class Event
    {
        public double scheduledTime;

        public Event(double currentTime)
        {
            scheduledTime = currentTime;
        }
    }
}
