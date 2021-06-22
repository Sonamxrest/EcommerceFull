const express = require('express')
const { verifyUser } = require('../middleware/auth')
const { array } = require('../middleware/upload')
const Booking = require('../models/booking')
const route = express.Router()
const Bokking = require("../models/booking")
const Order = require("../models/order")
const Product = require('../models/Product')



route.post("/order",verifyUser,(req,res)=>{
let newData =[]
let total =0
let id=""
Booking.findOne({User:req.user._id}).populate("Product.product").then((data)=>{
    id = data._id
data.Product.forEach(element => {
var stock = parseInt(element.product.Stock) -parseInt(element.qty)
Product.findByIdAndUpdate({_id:element.product._id},{
Stock : stock
})
    total += parseInt(element.product.Price) * element.qty
    newData.push(element)
})
const order =new Order({User:req.user._id, Product:newData,PhoneNumber:req.body.PhoneNumber,Total:total,Latitude:req.body.Latitude,Longtitude:req.body.Longtitude,Address:req.body.Address})
order.save().then((a)=>{
    Booking.findByIdAndDelete({_id:id}).then((r)=>{
        res.status(200).json({success:true,message:"Done"})
    })
})

})
})


route.get("/allMyOrder",verifyUser,(req,res)=>{
    Order.find({User:req.user._id}).populate('Product.product').populate('User').populate("EmployeeId").then((data)=>{
if(data)
{
    res.status(200).json({success:true,data:data})

}
else{
    res.status(200).json({success:true,data:[]})

}
    })
})
route.get("/allOrder",(req,res)=>{
    console.log("asd")
    Order.find().populate('Product.product').populate('User').populate("EmployeeId").then((data)=>{
        console.log(data)

        res.status(200).json({success:true,data:data})
    })
})


route.put("/acceptOrder",(req,res)=>{
    Order.findOneAndUpdate({_id:req.body.id},{
        OrderStatus:true
    }).then((result)=>{
        res.status(200).json({success:true,message:"done"})
    })
})

route.put("/cancelOrder",(req,res)=>{

Order.findOneAndDelete({_id:req.body.id}).then((data)=>{
    res.status(200).json({success:true,message:"done"})
})

})


route.put("/deliverOrder/:id",verifyUser,(req,res)=>{
console.log(req.params.id)
console.log(req.user.Name)
    Order.findOneAndUpdate({_id:req.params.id},{
EmployeeId:req.user._id

    }).then((data)=>{
        res.status(200).json({success:true,message:"done"})
    }).catch((err)=>{
        console.log(err)
    })
    
    })



module.exports = route