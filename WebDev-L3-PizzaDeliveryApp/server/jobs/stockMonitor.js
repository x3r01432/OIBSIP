const cron=require('node-cron');const Inventory=require('../models/Inventory');const {sendMail}=require('./mailer');
// Every hour: email admin about any item below threshold
cron.schedule('0 * * * *',async()=>{
  try{
    const low=await Inventory.find({$expr:{$lt:['$stock','$threshold']}});
    if(low.length&&process.env.ADMIN_EMAIL){
      const body=low.map(i=>`${i.type} ${i.name}: ${i.stock} left`).join('\n');
      await sendMail(process.env.ADMIN_EMAIL,'Low stock alert',body);
    }
  }catch(e){console.error('stock job',e.message);}
});
