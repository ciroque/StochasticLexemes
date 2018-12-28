package org.ciroque.lexeme

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll, MustMatchers, Matchers, WordSpecLike}
import scala.concurrent.duration._

class StochasticSpec
  extends TestKit(ActorSystem("LexemeStochasticSpec"))
  with ImplicitSender
  with WordSpecLike
  with Matchers
  with BeforeAndAfterAll {

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "Stochastic" must {
    "should respond to a LexemeRequest for 0 words" in {
      val stochastic = system.actorOf(Props(new Stochastic()))
      stochastic ! LexemeRequest(0)
      val Lexemes(words) = receiveOne(1 seconds)
      words.length shouldEqual 0
    }

    "should respond to a LexemeRequest for 3 words" in {
      val length = 3
      val stochastic = system.actorOf(Props(new Stochastic()))
      stochastic ! LexemeRequest(length)
      val Lexemes(words) = receiveOne(1 seconds)
      println(s"WORDS: $words")
      words.length shouldEqual length
    }
  }
}
