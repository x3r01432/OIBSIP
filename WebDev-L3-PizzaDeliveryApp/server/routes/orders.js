const r=require('express').Router();const Razorpay=require('razorpay');
const {auth,admin}=require('../middleware/auth');
const Order=require('../models/Order');const Inventory=require('../models/Inventory');
const rz=new Razorpay({key_id:process.env.RAZORPAY_KEY_ID||'test',key_secret:process.env.RAZORPAY_KEY_SECRET||'test'});
r.post('/create-payment',auth,async(req,res)=>{
  try{const o=await rz.orders.create({amount:(req.body.amount||299)*100,currency:'INR'});res.json(o);}
  catch{res.json({id:'order_test',amount:(req.body.amount||299)*100});} // test-mode fallback
});
r.post('/',auth,async(req,res)=>{
  const{base,sauce,cheese,veggies,amount,paymentId}=req.body;
  const order=await Order.create({user:req.user.id,base,sauce,cheese,veggies,amount,paymentId});
  // decrement stock
  await Inventory.updateOne({type:'base',name:base},{$inc:{stock:-1}});
  await Inventory.updateOne({type:'sauce',name:sauce},{$inc:{stock:-1}});
  await Inventory.updateOne({type:'cheese',name:cheese},{$inc:{stock:-1}});
  for(const v of veggies||[])await Inventory.updateOne({type:'veggie',name:v},{$inc:{stock:-1}});
  res.json(order);
});
r.get('/mine',auth,async(req,res)=>res.json(await Order.find({user:req.user.id}).sort('-createdAt')));
r.get('/all',auth,admin,async(req,res)=>res.json(await Order.find().populate('user','name email').sort('-createdAt')));
r.put('/:id/status',auth,admin,async(req,res)=>res.json(await Order.findByIdAndUpdate(req.params.id,{status:req.body.status},{new:true})));
module.exports=r;
