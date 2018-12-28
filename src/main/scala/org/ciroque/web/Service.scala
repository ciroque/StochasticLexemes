package org.ciroque.web

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.server.Directives._
import akka.util.Timeout
import com.typesafe.config.Config
import org.ciroque.lexeme.{LexemeRequest, Stochastic}

import scala.concurrent.ExecutionContextExecutor

trait Protocols {
}

trait Service extends Protocols {
  import scala.concurrent.duration._
  implicit val system: ActorSystem
  implicit def executor: ExecutionContextExecutor
  def config: Config
//  val logger: LoggingAdapter
  implicit def requestTimeout: Timeout = Timeout(5 seconds)
  println(s"... $system")
  val stochastic: ActorRef = system.actorOf(Props(new Stochastic()))
  val routes = {
    path("api") {
      get {
        complete {
          stochastic ! LexemeRequest(3)
          "Yes"
        }
      }
    }
  }
}
