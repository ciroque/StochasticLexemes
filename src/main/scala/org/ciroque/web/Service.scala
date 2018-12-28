package org.ciroque.web

import akka.pattern.ask
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.typesafe.config.Config
import org.ciroque.lexeme.{LexemeRequest, Lexemes, Stochastic}
import spray.json.{DefaultJsonProtocol, RootJsonFormat}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

import scala.concurrent.ExecutionContextExecutor

trait Protocols {
  import DefaultJsonProtocol._
  implicit val lexemesFormat: RootJsonFormat[Lexemes] = jsonFormat1(Lexemes)
}

trait Service extends Protocols {
  import scala.concurrent.duration._
  implicit val system: ActorSystem
  implicit def executor: ExecutionContextExecutor
  def config: Config
//  val logger: LoggingAdapter
  implicit def requestTimeout: Timeout = Timeout(5 seconds)
  val stochastic: ActorRef = system.actorOf(Props(new Stochastic()))
  lazy val routes: Route = {
    pathPrefix("api") {
      path("words") {
        get {
          val result = (stochastic ? LexemeRequest(7, 5)).mapTo[Lexemes]

          complete(result)
        }
      }
    }
  }
}
