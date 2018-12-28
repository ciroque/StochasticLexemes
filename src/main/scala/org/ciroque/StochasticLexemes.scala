package org.ciroque

import akka.actor.ActorSystem
import akka.event.LoggingAdapter
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import org.ciroque.web.Service

import scala.concurrent.ExecutionContextExecutor

object StochasticLexemes extends Service with App {
  implicit lazy val system: ActorSystem = ActorSystem("StochasticLexemes")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  override implicit val executor: ExecutionContextExecutor = system.dispatcher
  override val config = ConfigFactory.load()
  //  override val logger: LoggingAdapter = _
  Http()(system).bindAndHandle(routes , config.getString("http.interface"), config.getInt("http.port"))
}
