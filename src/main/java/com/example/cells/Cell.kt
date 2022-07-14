package com.example.cells

class Cell(private var _petri: Array<Array<Boolean>>) :
    iStatObservable{
    private var coordinates: ArrayList<PrimitiveCoordinates> = arrayListOf()
    private var statObservers: ArrayList<iStatObserver> = arrayListOf()
    fun getCoordinates() : ArrayList<PrimitiveCoordinates> {
        return coordinates
    }
    private var cells: ArrayList<Position> = arrayListOf()
    fun haveCells() : Boolean{
        return cells.size != 0
    }

    override fun addObserver(observer: iStatObserver) {
        if (!statObservers.contains(observer)) statObservers.add(observer)
    }

    override fun banishObserver(observer: iStatObserver) {
        if (statObservers.contains(observer)) statObservers.remove(observer)
    }

    override fun notifyObservers(state: Boolean, died: Int) {
        for (i in statObservers) {
            i.update(state, cells.size, died)
        }
    }
    private var bornCells: ArrayList<Position> = arrayListOf()
    private fun die(i: Int, position: Position) {
        cells.removeAt(i)
        _petri[position.posX][position.posY] = false
    }
    fun division() {
        coordinates.clear()
        val theDead: ArrayList<Int> = arrayListOf()
        var died = 0
        for(i in 0 until cells.size) {
            val neighbors = naturalSelection(cells[i])
            if(!(neighbors == 2 || neighbors == 3)) theDead.add(i)
        }
        for(i in 0 until cells.size) {
            miracleOfBirth(cells[i])
        }
        for(i in theDead.size - 1 downTo  0){
            die(theDead[i], cells[theDead[i]])
            died++
        }
        cells.addAll(bornCells)
        for(i in 0 until bornCells.size){
            _petri[bornCells[i].posX][bornCells[i].posY] = true
        }
        if(cells != null) {
            coordinates.add(PrimitiveCoordinates(cells))
        }
        if(cells.size != 0)notifyObservers(true, died)
        else notifyObservers(false, died)
        bornCells = arrayListOf()

    }
    private fun naturalSelection(position: Position) : Int
    {
        var neighbors = 0
        if((position.posX > 0 && position.posY > 0)
            && (position.posX < _petri.size && position.posY < _petri.size)) {
            for(i in 0 until 3) {
                for(j in 0 until 3) {
                    if(i == 1 && j == 1) continue
                    if(i + position.posX - 1 < _petri.size
                        && j + position.posY - 1 < _petri.size && i + position.posX - 1 > 0
                        && position.posY - 1 > 0) {
                        if(_petri[position.posX - 1 + i][position.posY - 1 + j]) neighbors++
                    }
                }
            }
        }
        return neighbors
    }
    private fun miracleOfBirth(position: Position)
    {
        if((position.posX > 0 && position.posY > 0)
            && (position.posX < _petri.size && position.posY < _petri.size)) {
            for(i in 0 until 3) {
                for(j in 0 until 3) {
                    if(i == 1 && j == 1) continue
                    if(i + position.posX - 1 < _petri.size
                        && j + position.posY - 1 < _petri.size && i + position.posX - 1 > 0
                        && position.posY - 1 > 0) {
                        if(!_petri[position.posX - 1 + i][position.posY - 1 + j])
                            born(Position(position.posX - 1 + i, position.posY - 1 + j))
                    }
                }
            }
        }
    }
    private fun born(position: Position) {
        var neighbors = 0
        for(j in 0 until 3) {
            for(i in 0 until 3) {
                if(i == 1 && j == 1) continue
                if(i + position.posX - 1 < _petri.size
                    && j + position.posY - 1 < _petri.size && i + position.posX - 1 > 0
                    && position.posY - 1 > 0) {
                    if(_petri[i + position.posX - 1][j + position.posY - 1]) neighbors++
                }
            }
        }
        if(neighbors == 3 && contains(position)) {
            bornCells.add(position)
        }
    }
    private fun contains(position: Position) : Boolean{
        for(i in 0 until bornCells.size){
            if(bornCells[i].posX == position.posX && bornCells[i].posY == position.posY)
            {
                return false
            }
        }
        return true
    }
    fun addCell(position: Position) {
        if(!cells.contains(position)) {
            cells.add(position)
        }
    }
}