package com.twitter.gizzard.scheduler

import com.twitter.xrayspecs.TimeConversions._
import net.lag.configgy.Config
import org.specs.mock.{ClassMocker, JMocker}
import org.specs.Specification

object PrioritizingJobSchedulerSpec extends ConfiguredSpecification with JMocker with ClassMocker {
  "PrioritizingJobScheduler" should {
    val low = mock[JobScheduler[Job]]
    val medium = mock[JobScheduler[Job]]
    val high = mock[JobScheduler[Job]]
    val prioritizingScheduler = new PrioritizingJobScheduler(Map(3 -> low, 2 -> medium, 1 -> high))

    "apply" in {
      val job = mock[Job]

      expect { one(low).put(job) }
      prioritizingScheduler.put(3, job)
      expect { one(medium).put(job) }
      prioritizingScheduler.put(2, job)
      expect { one(high).put(job) }
      prioritizingScheduler.put(1, job)
    }

    "retryErrors" in {
      expect {
        one(low).retryErrors()
        one(medium).retryErrors()
        one(high).retryErrors()
      }

      prioritizingScheduler.retryErrors()
    }

    "pause" in {
      expect {
        one(low).pause()
        one(medium).pause()
        one(high).pause()
      }

      prioritizingScheduler.pause()
    }

    "shutdown" in {
      expect {
        one(low).shutdown()
        one(medium).shutdown()
        one(high).shutdown()
      }

      prioritizingScheduler.shutdown()
    }

    "resume" in {
      expect {
        one(low).resume()
        one(medium).resume()
        one(high).resume()
      }

      prioritizingScheduler.resume()
    }
  }
}
