package entity

/** Connection is a Class which represents a connection of two ConnectionPoints of a Tile.
 *
 * @param connectionPoint1 represents the first ConnectionPoint
 * @param connectionPoint2 represents the second ConnectionPoint
 * */
class Connection(var connectionPoint1 : ConnectionPoint,var connectionPoint2 : ConnectionPoint)  {

    /** A Method that returns the first ConnectionPoint.
     * @return the first ConnectionPoint*/
    fun first() : ConnectionPoint{
        return connectionPoint1;
    }

    /** A Method that returns the second ConnectionPoint.
     * @return the second ConnectionPoint*/
    fun second() : ConnectionPoint{
        return connectionPoint2;
    }

    /** A Method which returns a List of the two ConnectionPoints.
     * @return A read-only list containing both ConnectionPoints
     * */
    fun toList() : List<ConnectionPoint>{
        return listOf(connectionPoint1, connectionPoint2)
    }

    /** A Method which returns a String in format of the Pair<>-toString-Method.
     * @return a string of format (ConnectionPoint1, ConnectionPoint2)*/
    override fun toString() : String{
        return "($connectionPoint1, $connectionPoint2)"
    }
}