const r=require('express').Router();
// Static menu of varieties + builder options
r.get('/',(req,res)=>res.json({
  varieties:['Margherita','Pepperoni','Veggie Supreme','BBQ Chicken'],
  bases:['Thin Crust','Thick Crust','Cheese Burst','Whole Wheat','Gluten Free'],
  sauces:['Tomato','Pesto','BBQ','Alfredo','Peri Peri'],
  cheeses:['Mozzarella','Cheddar','Parmesan'],
  veggies:['Onion','Capsicum','Mushroom','Olives','Corn','Jalapeno']
}));
module.exports=r;
