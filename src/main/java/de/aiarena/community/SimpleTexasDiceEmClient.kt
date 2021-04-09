package de.aiarena.community

import kotlin.streams.toList

fun main(args: Array<String>){
    SimpleTexasDiceEmClient("localhost",1805,"6c4f64996816589c89d97f69ae2f4285063982a6db7f2592007c977ea1e2df93",true)

    while(true){
        Thread.sleep(5000)
    }
}

class SimpleTexasDiceEmClient(host: String, port: Int, private val secret: String, debug: Boolean = true) {
    private val client = AMTPClient(host,port,secret,this::onMessage,debug=debug)
    private val keys = listOf("Number-1","Number-2","Number-3","Number-4","Number-5","Number-6","Triple","Four","Full-House","Street-Short","Street-Long","Five","Joker")
    private var keyIndex = 0

    private fun onMessage(msg: MessageFromServer, myTurn: Boolean){
        if(myTurn){
            val myDiceS = msg.headers["PlayerDice"]!!
            val boardDiceS = msg.headers["BoardDice"]!!

            val myDice = myDiceS.split(",").toList().stream().map{it.toInt()}.toList()
            val boardDice = boardDiceS.split(",").toList().stream().map{it.toInt()}.toList()

            val dice = ArrayList<Int>()
            dice.addAll(myDice)
            dice.addAll(boardDice)
            dice.sortDescending()
            val used = dice.subList(0,5)

            val message = MessageToServer("GAME","AMTP/0.0",hashMapOf(
                "Action" to "Put",
                "Slot" to keys[keyIndex],
                "Combination" to used.joinToString(",")
            ))
            keyIndex++
            client.send(message)
        }
    }
}
