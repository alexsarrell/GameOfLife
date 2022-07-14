package com.example.cells

class Petri(private var _field: Int, private var step: Int){
    private var field: Array<Array<Boolean>> = Array(_field) { Array(_field) { false }}
    private var cells: Boolean = false
    private var cell: Cell = Cell(field)
    fun addCell(position: Position) {
        if(position.posX >= 0 && position.posY >= 0 &&
            position.posX < field.size * step && position.posY < field.size * step) {
            cells = true
            val squarePosition = Position(
                position.posX / step, position.posY / step)
            field[squarePosition.posX][squarePosition.posY] = true
            cell.addCell(squarePosition)
        }
    }
    fun assistantCheck() : Boolean{
        return cells
    }
    fun getStatisticObserver() : iStatObservable{
        return cell
    }
    fun startSim() : ArrayList<PrimitiveCoordinates> {
        return if(cell.haveCells()){
            cell.division()
            cell.getCoordinates()
        } else arrayListOf()
    }
}