package com.twitter.gizzard.thrift

import conversions.Sequences._
import shards._
import scheduler.PrioritizingJobScheduler

class JobManagerService(scheduler: PrioritizingJobScheduler[_]) extends JobManager.Iface {
  def retry_errors() = scheduler.retryErrors()
  def stop_writes() = scheduler.pause()
  def resume_writes() = scheduler.resume()

  def retry_errors_for(priority: Int) = scheduler(priority).retryErrors()
  def stop_writes_for(priority: Int) = scheduler(priority).pause()
  def resume_writes_for(priority: Int) = scheduler(priority).resume()
  def is_writing(priority: Int) = !scheduler(priority).isShutdown
}
