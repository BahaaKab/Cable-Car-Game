package service

import entity.*
import kotlin.random.Random

@Suppress("UNUSED_PARAMETER","UNUSED","UndocumentedPublicFunction","UndocumentedPublicClass","EmptyFunctionBlock")
class SetupService(private val rootService: RootService) : AbstractRefreshingService() {

    /**
     * Creates and starts a new local game in hotseat mode.
     * **/
    fun startLocalGame(players: List<Player>?, tilesRotatable: Boolean, aISpeed: Int) {
        //Since our data types are inconsistent, I have to recast here
        val drawPile = initializeDrawPile()!!.toMutableList()
        //At the beginning the playing field is empty. It will later be filled with tiles
        val board: Array<Array<Tile?>> = arrayOf()
        val firstState = State(drawPile,
            players!![0],
            board,
            players)
        CableCar(tilesRotatable,
            aISpeed,
            false,
            GameMode.HOTSEAT,
            History(),
            firstState)
    }

    fun startNetworkGame(
        isHostPlayer: Boolean,
        players: List<PlayerInfo?>?,
        tilesRotatable: Boolean,
        tileIDs: List<Int>?,
        aISpeed: Int
    ) {
    }

    private fun createPlayers(playerInfos: List<PlayerInfo>) {}

    /**
     * Returns the drawPile.
     * If played locally we just return the drawPile.
     * If Played over the network we import the order of the drawPile from the GameInitMessage
     * **/
    private fun initializeDrawPile(): List<GameTile>? {
        var online = false
        val players = rootService.cableCar!!.currentState!!.players
        val game = rootService.ioService
        for(i in players.indices){
            if (players[i].playerType == PlayerType.NETWORK) online = true
            break
        }
        if (!online){
            //local game just returns a list
            val gameTiles = game!!.getTilesFromCSV()
            return gameTiles
        } else {
            //Some Network magic needs to happen here...
            //Keine Ahnung wie die GameInitMessage funktioniert
            return null
        }
    }

}