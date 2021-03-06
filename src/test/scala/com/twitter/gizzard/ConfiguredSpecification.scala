package com.twitter.gizzard

import net.lag.configgy.Configgy
import org.specs.Specification


abstract class ConfiguredSpecification extends Specification {
  Configgy.configure("config/test.conf")
  val config = Configgy.config
}
