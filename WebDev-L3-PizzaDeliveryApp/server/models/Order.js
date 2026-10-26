const mongoose=require('mongoose');
module.exports=mongoose.model('Order',new mongoose.Schema({
  user:{type:mongoose.Schema.Types.ObjectId,ref:'User'},
  base:String,sauce:String,cheese:String,veggies:[String],
  amount:Number,paymentId:String,
  status:{type:String,enum:['Order Received','In Kitchen','Sent to Delivery'],default:'Order Received'}
},{timestamps:true}));
