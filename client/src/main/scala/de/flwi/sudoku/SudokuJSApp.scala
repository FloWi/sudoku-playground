package de.flwi.sudoku

import de.flwi.sudoku.model.logic.{SudokuParser, Sudoku}
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


    val mountNode: Node = dom.document.getElementById("sudoku-content")
    val mounted = ReactDOM.render(Example(), mountNode)
  }
}
