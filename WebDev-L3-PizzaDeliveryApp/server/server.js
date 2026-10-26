require('dotenv').config();
const express=require('express');const cors=require('cors');const mongoose=require('mongoose');
const app=express();app.use(cors());app.use(express.json());
app.use('/api/auth',require('./routes/auth'));
app.use('/api/pizza',require('./routes/pizza'));
app.use('/api/orders',require('./routes/orders'));
app.use('/api/inventory',require('./routes/inventory'));
app.get('/',(req,res)=>res.json({status:'Pizza API running'}));
require('./jobs/stockMonitor'); // node-cron low-stock email job
const PORT=process.env.PORT||5000;
mongoose.connect(process.env.MONGO_URI||'mongodb://localhost:27017/pizzaapp')
  .then(()=>app.listen(PORT,()=>console.log('Server on '+PORT)))
  .catch(e=>console.error('Mongo error',e));
