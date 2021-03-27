package de.aiarena.community

fun main(args: Array<String>){
    SimpleYahtzeeClient("ai-arena.de",1805,"YOUR KEY",false)

    while(true){
        Thread.sleep(5000)
    }
}

class SimpleYahtzeeClient(host: String, port: Int, private val secret: String, debug: Boolean = true) {
    private val client = AMTPClient(host,port,secret,this::onMessage,debug=debug)
    private val keys = listOf("Number-1","Number-2","Number-3","Number-4","Number-5","Number-6","Triple","Four","Full-House","Street-Short","Street-Long","Five","Chance")
    private var keyIndex = 0

    private fun onMessage(msg: MessageFromServer, myTurn: Boolean){

        if(myTurn){
            val message = MessageToServer("GAME","AMTP/0.0",hashMapOf("Action" to "Put", "Slot" to keys[keyIndex]))
            client.send(message)
            keyIndex++
        }
    }
}
