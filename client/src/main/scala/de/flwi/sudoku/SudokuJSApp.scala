package de.flwi.sudoku

import de.flwi.sudoku.model.logic._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.Attrs
import japgolly.scalajs.react.vdom.all.svg._
import japgolly.scalajs.react.vdom.prefix_<^._
import org.scalajs.dom
import org.scalajs.dom.raw.Node
import org.scalajs.jquery.{JQuery, jQuery}

import scala.collection.immutable.IndexedSeq
import scala.scalajs.js
import scala.scalajs.js.Dynamic.{global => glo}
import scala.scalajs.js.annotation.JSExport

import scalatags.Text.all._

@JSExport
object ExampleJS {

  @JSExport
  def main(): Unit = {
    g.document.getElementById("scalajsShoutOut").textContent = SharedMessages.itWorks
  }

  /** Computes the square of an integer.
    *  This demonstrates unit testing.
    */
  def square(x: Int): Int = x*x
}



object SudokuJSApp extends js.JSApp {

  val mountNode: Node = dom.document.getElementById("sudoku-content")

  def main(): Unit = {

  }

  @JSExport
  def renderSudoku(sudokuString: String) = {
    SudokuParser.parse(sudokuString) match {
      case Left(error) => jQuery("#sudoku-content").append(s"Sudoku kapott: $error")
      case Right(sudoku) => render(sudoku, sudokuString)
    }
  }

  def render(sudoku: Sudoku, sudokuString: String) = {

    val sudokuRender = <.table(^.`class` := "sudoku",
      <.tbody(
        renderBoxRows(sudoku)
      )
    ).render

    ReactDOM.render(timerComponent(), mountNode.appendChild(dom.document.createElement("div")))

    ReactDOM.render(headerRender(sudokuString), mountNode.appendChild(dom.document.createElement("div")))

    val jsDomExample = new SudokuSvg(scalatags.JsDom)
    mountNode.appendChild(jsDomExample.svgFrag.render)

    //ReactDOM.render(sudokuRender, mountNode.appendChild(dom.document.createElement("div")))

    val cell: Unit = renderUnsolvedCell(CandidateList(Coordinate(0), candidates = 1.to(9).map(_.toByte).toSet))
    ReactDOM.render(cell, mountNode.appendChild(dom.document.createElement("div")))
  }


  def headerRender(sudokuString: String) = {
    <.h3(s"Render of sudoku: $sudokuString").render
  }

  def renderBoxRows(sudoku: Sudoku) = {
    sudoku.boxes.groupBy(_.boxCoordinate.row).toList.sortBy(_._1).map { case (boxRowIndex, boxes) =>
      <.tr(
        boxes.map { box =>
          <.td(
            renderBox(box, sudoku)
          )
        }
      )
    }
  }

  def renderBox(box: Box, sudoku: Sudoku) = {
    <.table(
      <.tbody(
        sudoku.getCellsOf(box).groupBy(_.coordinate.row).toIndexedSeq.sortBy(_._1).map { case (rowIndex, cells) =>
          <.tr(
            cells.sortBy(_.coordinate.column).map(cell => renderCell(cell, sudoku))
          )
        }
      )
    )
  }

  def renderCell(cell: Cell, sudoku: Sudoku) = {
    cell match {
      case SolvedCell(cellValue, coordinate) => <.td(<.b(cellValue))
      case UnsolvedCell(coordinate) =>
        <.td(
          sudoku.calcCandidates(coordinate)
            .map(_.candidates)
            .getOrElse(Set.empty)
            .toList
            .sorted
            .mkString(", "))
    }
  }

  def timerComponent() = {
    case class State(secondsElapsed: Long)

    class Backend($: BackendScope[Unit, State]) {
      var interval: js.UndefOr[js.timers.SetIntervalHandle] =
        js.undefined

      def tick =
        $.modState(s => State(s.secondsElapsed + 1))

      def start = Callback {
        interval = js.timers.setInterval(1000)(tick.runNow())
      }

      def clear = Callback {
        interval foreach js.timers.clearInterval
        interval = js.undefined
      }

      def render(s: State) =
        <.div("Seconds elapsed: ", s.secondsElapsed)
    }

    val Timer = ReactComponentB[Unit]("Timer")
      .initialState(State(0))
      .renderBackend[Backend]
      .componentDidMount(_.backend.start)
      .componentWillUnmount(_.backend.clear)
      .buildU

    Timer()

  }

//  def renderUnsolvedCell(candidateList: CandidateList) = {
//
//    case class UnsolvedCellState(candidateList: CandidateList, highlightedCandidate: Option[Byte])
//    case class CandidateDetails(candidateValue: Byte, x: Int, y: Int, row: Int, column: Int)
//
//    val UnsolvedCell = ReactComponentB[Unit]("UnsolvedCell")
//      .initialState(UnsolvedCellState(candidateList, None))
//      .render_S( state => {
//        val candidateDetails = for (row <- 0 to 2;
//                                     column <- 0 to 2;
//                                     candidateValue = (row * 3 + column + 1).toByte;
//                                     x = column * 20;
//                                     y = row * 20
//                                     if state.candidateList.candidates.contains(candidateValue)
//        ) yield text(
//          fill := "black",
//          candidateValue.toString
//        )
//
//        g(candidateDetails)
//      }).build
//
//    UnsolvedCell()
//  }
}
class SudokuSvg[Builder, Output <: FragT, FragT]
(val bundle: scalatags.generic.Bundle[Builder, Output, FragT]) {

  val svgFrag = {
    import bundle.implicits._
    import bundle.svgTags._
    import bundle.svgAttrs._

    svg(height := "800", width := "500")(
      polyline(
        points := "20,20 40,25 60,40 80,120 120,140 200,180",
        fill := "none",
        stroke := "black",
        strokeWidth := "3"
      )
    )
  }


}

