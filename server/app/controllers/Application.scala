package controllers

import de.flwi.sudoku.SharedMessages
import de.flwi.sudoku.model.logic.SudokuParser
import play.api.mvc._
import play.api._

object Application extends Controller {

  def index = Action { implicit request =>
    Ok(views.html.index(SharedMessages.itWorks))
  }

  def newSudoku() = sudoku("000105000140000670080002400063070010900000003010090520007200080026000035000409000")

  def sudoku(inputString: String) = Action { implicit request =>

    SudokuParser.parse(inputString) match {
      case Left(error) => BadRequest(error)
      case Right(sudoku) => Ok(views.html.sudokuBoard(sudoku))
    }
  }

  def newSudokuJS() = sudokuJS("000105000140000670080002400063070010900000003010090520007200080026000035000409000")

  def sudokuJS(inputString: String) = Action { implicit request =>
    SudokuParser.parse(inputString) match {
      case Left(error) => BadRequest(error)
      case Right(sudoku) => Ok(views.html.sudokuJS(inputString))
    }
  }
}
