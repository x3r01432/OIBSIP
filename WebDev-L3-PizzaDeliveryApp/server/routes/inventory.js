const r=require('express').Router();const {auth,admin}=require('../middleware/auth');
const Inventory=require('../models/Inventory');
r.get('/',auth,admin,async(req,res)=>res.json(await Inventory.find()));
r.put('/:id',auth,admin,async(req,res)=>res.json(await Inventory.findByIdAndUpdate(req.params.id,{stock:req.body.stock},{new:true})));
r.post('/seed',async(req,res)=>{
  await Inventory.deleteMany({});
  const seed=[['base','Thin Crust'],['base','Thick Crust'],['sauce','Tomato'],['sauce','BBQ'],['cheese','Mozzarella'],['veggie','Onion']];
  await Inventory.insertMany(seed.map(([type,name])=>({type,name,stock:50,threshold:20})));
  res.json({msg:'seeded'});
});
module.exports=r;
