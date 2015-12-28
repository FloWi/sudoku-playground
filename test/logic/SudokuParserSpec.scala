package logic

import org.specs2.matcher.MatchResult
import org.specs2.mutable._

import scala.collection.immutable.IndexedSeq

class SudokuParserSpec extends Specification {

  "SudokuParser" should {

    "parse the X-Wing sample sudoku correctly" in {

      val inputString = "100000569492056108056109240009640801064010000218035604040500016905061402621000005"
      val Right(actual) = SudokuParser.parse(inputString)

      actual.cells.length must_== 81
      actual.cells.count(_.isInstanceOf[SolvedCell]) must_== inputString.toCharArray.count(_ != '0')
      actual.cells.count(_.isInstanceOf[UnsolvedCell]) must_== inputString.toCharArray.count(_ == '0')

      actual.cells
        .map {
          case x: SolvedCell => x.value.toString
          case x: UnsolvedCell => "0"
        }
        .mkString must_== inputString
    }

    "reject incorrect sudokus properly" in {

      val tooLongInput = "1000005694920561080561092400096408010640100002180356040405000169050614026210000050"
      SudokuParser.parse(tooLongInput) mustEqual Left("input too long")

      val tooShortInput = "10000056949205610805610924000964080106401000021803560404050001690506140262100000"
      SudokuParser.parse(tooShortInput) mustEqual Left("input too short")

      val invalidInput = "10000XX69492056108056109240009640801064010000218035604040500016905061402621000005"
      SudokuParser.parse(invalidInput) mustEqual Left("'X' is not valid at r0|c5|b1")
    }

    "calculate the row, column and box properly" in {

      val matchResults: IndexedSeq[MatchResult[Any]] = 0.to(8).flatMap { rowIndex => List(
        Coordinate(index = (rowIndex * 9 + 0).toByte) mustEqual Coordinate(row = rowIndex.toByte, column = 0, box = (rowIndex /3 * 3 + 0).toByte),
        Coordinate(index = (rowIndex * 9 + 1).toByte) mustEqual Coordinate(row = rowIndex.toByte, column = 1, box = (rowIndex /3 * 3 + 0).toByte),
        Coordinate(index = (rowIndex * 9 + 2).toByte) mustEqual Coordinate(row = rowIndex.toByte, column = 2, box = (rowIndex /3 * 3 + 0).toByte),
        Coordinate(index = (rowIndex * 9 + 3).toByte) mustEqual Coordinate(row = rowIndex.toByte, column = 3, box = (rowIndex /3 * 3 + 1).toByte),
        Coordinate(index = (rowIndex * 9 + 4).toByte) mustEqual Coordinate(row = rowIndex.toByte, column = 4, box = (rowIndex /3 * 3 + 1).toByte),
        Coordinate(index = (rowIndex * 9 + 5).toByte) mustEqual Coordinate(row = rowIndex.toByte, column = 5, box = (rowIndex /3 * 3 + 1).toByte),
        Coordinate(index = (rowIndex * 9 + 6).toByte) mustEqual Coordinate(row = rowIndex.toByte, column = 6, box = (rowIndex /3 * 3 + 2).toByte),
        Coordinate(index = (rowIndex * 9 + 7).toByte) mustEqual Coordinate(row = rowIndex.toByte, column = 7, box = (rowIndex /3 * 3 + 2).toByte),
        Coordinate(index = (rowIndex * 9 + 8).toByte) mustEqual Coordinate(row = rowIndex.toByte, column = 8, box = (rowIndex /3 * 3 + 2).toByte)
      )
      }
      matchResults
    }


  }
}
