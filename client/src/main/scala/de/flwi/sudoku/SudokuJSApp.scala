package de.flwi.sudoku

import de.flwi.sudoku.model.logic._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import org.scalajs.dom
import org.scalajs.dom.raw.Node
import org.scalajs.jquery.{JQuery, jQuery}

import scala.scalajs.js
import scala.scalajs.js.Dynamic.{global => g}
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

    val sudokuRender = <.table(^.`class`:="table table-bordered",
      <.tbody(
        renderBoxRows(sudoku)
      )
    ).render

    ReactDOM.render(timerComponent(), mountNode.appendChild(dom.document.createElement("div")))

    ReactDOM.render(headerRender(sudokuString), mountNode.appendChild(dom.document.createElement("div")))
    ReactDOM.render(sudokuRender, mountNode.appendChild(dom.document.createElement("div")))
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
    <.table(^.`class`:="table table-bordered",
      <.tbody(
        sudoku.getCellsOf(box).groupBy(_.coordinate.row).toIndexedSeq.sortBy(_._1).map { case(rowIndex, cells) =>
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
  def sample() = {

    type State = Vector[String]

    class Backend($: BackendScope[Unit, State]) {
      def render(s: State) =   // ← Accept props, state and/or propsChildren as argument
        <.div(
          <.div(s.length, " items found:"),
          <.ol(s.map(i => <.li(i))))
    }

    val Example = ReactComponentB[Unit]("Example")
      .initialState(Vector("hello", "world"))
      .renderBackend[Backend]  // ← Use Backend class and backend.render
      .buildU

    Example()

  }
}
