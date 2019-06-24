package org.ciroque.lexeme

import akka.actor.Actor

import scala.io.Source
import scala.util.Random

class Stochastic extends Actor {
  override def receive: Receive = {
    case request: AdjectiveRequest =>
      sender() ! Stochastic.selectRandomWords(request, Stochastic.adjectives)

    case request: NounRequest =>
      sender() ! Stochastic.selectRandomWords(request, Stochastic.nouns)

    case request: WordRequest =>
      sender() ! Stochastic.selectRandomWords(request, Stochastic.words)
  }
}

object Stochastic {
  lazy val adjectives: Array[String] = loadAdjectives()
  lazy val nouns: Array[String] = loadNouns()
  lazy val words: Array[String] = loadWords()
  lazy val random: Random = new Random(System.nanoTime())

  def selectRandomWords(request: LexemeRequest, source: Array[String]): Lexemes = {
    candidateWords(request, source) match {
      case Array() => Lexemes(List[String]())
      case candidates =>
        val indexes = Random.shuffle(candidates.indices.toList).take(request.howMany)
        var lexes = indexes.map(index => candidates(index)).toList
        Lexemes(lexes)
    }
  }

  private def loadAdjectives(): Array[String] = Source.fromResource("adjectives").getLines().toArray.sorted
  private def loadNouns(): Array[String] = Source.fromResource("nouns").getLines().toArray.sorted
  private def loadWords(): Array[String] = Source.fromResource("words").getLines().toArray.sorted


  /* ------ */
  private def candidateWords(request: LexemeRequest, source: Array[String]): Array[String] = {
    val filtered: Array[String] = if(request.maxWordLength == -1) source
    else source.filter(word => word.length <= request.maxWordLength)

    (if(request.minWordLength == -1) filtered
    else filtered.filter(word => word.length >= request.minWordLength)).toArray[String]
  }
}
