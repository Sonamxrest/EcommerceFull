const bodyParser = require('body-parser')
const express = require('express')
const db = require('./db')
const WebSocket = require('ws')
const http = require('http')
const { json } = require('express')
const app =express()
const server = http.createServer(app)
const wss = new WebSocket.Server({server:server})

const route = require('./routes/UserRoutes')
const productRoute = require('./routes/productRoutes')
const bookRoute = require('./routes/bookingRoutes')
const orderRoute = require('./routes/orderRoutes.js')



app.use(bodyParser.urlencoded({extended:false}))
app.use(express.json())

app.use('/local',express.static(__dirname + "/images"))

let client=0

wss.on('connection', function connection(ws) {

    client++
    console.log(client)
    
        ws.on('message', function incoming(message) {


           newM = JSON.parse(message)
           console.log(newM)
            
               
          wss.broadcast( JSON.stringify({message:newM.message,profile:newM.profile}))

           });
    
           ws.on('close',(daat)=>{
            client--
            console.log(client)
             console.log("disconnected")
           })
    
    
        
      })
    
     
      wss.broadcast = function broadcast(msg) {   
      wss.clients.forEach(function each(client) {
            client.send(msg);         
         });
     };

app.use(route)
app.use(productRoute)
app.use(bookRoute)
app.use(orderRoute)

server.listen(3000,()=>{console.log("started at 3000")})