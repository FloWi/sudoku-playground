package logic

import org.specs2.matcher.{Matcher, MatchResult}
import org.specs2.mutable._

import scala.collection.immutable.Iterable

class SudokuSolverSpec extends Specification {

  "SudokuSolver" should {

    "find all possible candidates" in {

      val Right(xWingBoard) = SudokuParser.parse("100000569492056108056109240009640801064010000218035604040500016905061402621000005")

      val candidates = Map(
        Coordinate(row = 0, column = 1) -> Set(3, 7, 8),
        Coordinate(row = 0, column = 2) -> Set(3, 7),
        Coordinate(row = 0, column = 3) -> Set(2,3,4,7,8),
        Coordinate(row = 0, column = 4) -> Set(2,7,8),
        Coordinate(row = 0, column = 5) -> Set(2,3,4,7,8),
        Coordinate(row = 1, column = 3) -> Set(3,7),
        Coordinate(row = 1, column = 7) -> Set(3,7),
        Coordinate(row = 2, column = 0) -> Set(3,7,8),
        Coordinate(row = 2, column = 4) -> Set(7,8),
        Coordinate(row = 2, column = 8) -> Set(3,7),
        Coordinate(row = 3, column = 0) -> Set(3,5,7),
        Coordinate(row = 3, column = 1) -> Set(3,7),
        Coordinate(row = 3, column = 5) -> Set(2,7),
        Coordinate(row = 3, column = 7) -> Set(2,3,5,7),
        Coordinate(row = 4, column = 0) -> Set(3,5,7),
        Coordinate(row = 4, column = 3) -> Set(2,7,8,9),
        Coordinate(row = 4, column = 5) -> Set(2,7,8),
        Coordinate(row = 4, column = 6) -> Set(3,7,9),
        Coordinate(row = 4, column = 7) -> Set(2,3,5,7,9),
        Coordinate(row = 4, column = 8) -> Set(3,7),
        Coordinate(row = 5, column = 3) -> Set(7,9),
        Coordinate(row = 5, column = 7) -> Set(7,9),
        Coordinate(row = 6, column = 0) -> Set(3,7,8),
        Coordinate(row = 6, column = 2) -> Set(3,7),
        Coordinate(row = 6, column = 4) -> Set(2,7,8,9),
        Coordinate(row = 6, column = 5) -> Set(2,3,7,8),
        Coordinate(row = 6, column = 6) -> Set(3,7,9),
        Coordinate(row = 7, column = 1) -> Set(3,7,8),
        Coordinate(row = 7, column = 3) -> Set(3,7,8),
        Coordinate(row = 7, column = 7) -> Set(3,7,8),
        Coordinate(row = 8, column = 3) -> Set(3,4,7,8,9),
        Coordinate(row = 8, column = 4) -> Set(7,8,9),
        Coordinate(row = 8, column = 5) -> Set(3,4,7,8),
        Coordinate(row = 8, column = 6) -> Set(3,7,9),
        Coordinate(row = 8, column = 7) -> Set(3,7,8,9)
      )

//      def m1: Matcher[(Coordinate, Set[Int])] = { (coord: Coordinate, cand: Set[Int]) =>
//        ( s.startsWith("hello"), s+" doesn't start with hello")
//      }


      candidates.map {
        case (coord, cand) =>
          val actualCandidates: Option[CandidateList] = xWingBoard.calcCandidates(coord)
          actualCandidates.map(_.candidates) aka s"$coord" must_== Some(cand.map(_.toByte))
      }.toIndexedSeq
    }
  }

}
