package org.ciroque.lexeme

import akka.actor.Actor

import scala.io.Source
import scala.util.Random

class Stochastic extends Actor {
  override def receive: Receive = {
    case request: LexemeRequest =>
      sender() ! Stochastic.selectRandomWords(request)
  }
}

object Stochastic {
  lazy val words: Array[String] = loadWords()
  lazy val random: Random = new Random(System.nanoTime())

  def selectRandomWords(request: LexemeRequest): Lexemes = {
    val candidates = if(request.maxWordLength == -1) words
    else words.filter(word => word.length <= request.maxWordLength)

    val lexemes = (for(
      index <- 1 to request.howMany ;
      randomIndex = random.nextInt(candidates.length)
    ) yield candidates(randomIndex)).toList

    Lexemes(lexemes)
  }

  def loadWords(): Array[String] = {
    Source.fromResource("words").getLines().toArray.sorted
  }
}
