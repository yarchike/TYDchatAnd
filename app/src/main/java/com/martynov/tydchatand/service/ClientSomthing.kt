package com.martynov.tydchatand.service

import java.io.*
import java.net.Socket
import java.text.SimpleDateFormat
import java.util.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ClientSomthing (val adress: String, val port: Int, val name: String){
    var socket: Socket? = Socket(adress,port)
//    lateinit var inputUser: BufferedReader
//    lateinit var inServer: BufferedReader
//    lateinit var outServer: BufferedWriter

    companion object{

        var inputUser: BufferedReader? = null
        var inServer: BufferedReader? = null
        var outServer: BufferedWriter? = null
        var userName: String? = null
        var time : Date? = null
        var dTime : String? = null
        var dt1: SimpleDateFormat? = null


    }


    init{
        try{
            socket = Socket(adress, port)
        }catch (e : IOException) {
            // Сокет должен быть закрыт при любой
            // ошибке, кроме ошибки конструктора сокета:
            System.err.println("Socket failed");
        }
        try{
            inputUser = BufferedReader(InputStreamReader(System.`in`))
            inServer = BufferedReader(InputStreamReader(socket?.getInputStream()))
            outServer = BufferedWriter(OutputStreamWriter(socket?.getOutputStream()))
            this.pressNickname()
            ReadMsg().start() // нить читающая сообщения из сокета в бесконечном цикле
            WriteMsg().start()

        }catch (e : IOException){
            downService()
        }

    }

    private fun pressNickname() {
        try {
            userName = name
            outServer?.write("Hello ${userName.toString()} \n")
            outServer?.flush()
        } catch (ignored: IOException) {
        }
    }
    fun downService() {
        try {
            if (!socket!!.isClosed) {
                socket?.close()
                inServer?.close()
                outServer?.close()
            }
        } catch (ignored: IOException) {
        }
    }


    
    inner class ReadMsg : Thread() {

        override fun run() {
            var str: String? = null
            try{
                while (true){
                    str = ClientSomthing.inServer?.readLine()
                    if (str.equals("stop")) {
                        downService(); // харакири
                        break; // выходим из цикла если пришло "stop"
                    }
                    println(str);
                }
            }catch (ignored: IOException) {
                downService()
            }
        }
    }
    inner class WriteMsg : Thread(){
        override fun run() {
            while (true){
                var userWord: String? = null
                try{
                    time = Date()
                    dt1 = SimpleDateFormat("HH:mm:ss")
                    dTime = dt1?.format(time)
                    userWord = inputUser?.readLine()
                    if (userWord.equals("stop")) {
                        outServer?.write("stop" + "\n")
                        downService() // харакири
                        break
                    }else{
                        outServer?.write("(" + dTime + ") " + userName + ": " + userWord + "\n")
                    }
                    outServer?.flush()
                }catch (e:IOException){
                    downService()
                }
            }
        }
    }


}