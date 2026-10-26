import {useEffect,useState} from 'react';import api from '../api';
export default function Builder(){
  const[opt,setOpt]=useState({bases:[],sauces:[],cheeses:[],veggies:[]});
  const[sel,setSel]=useState({base:'',sauce:'',cheese:'',veggies:[]});
  useEffect(()=>{api.get('/pizza').then(r=>setOpt(r.data)).catch(()=>{});},[]);
  const order=async()=>{
    const pay=await api.post('/orders/create-payment',{amount:299}); // Razorpay test-mode order
    // In test mode, clicking Success confirms:
    await api.post('/orders',{...sel,amount:299,paymentId:pay.data.id});
    alert('Order placed! Track status on your dashboard.');
  };
  const pick=(k,v)=>setSel({...sel,[k]:v});
  const toggleVeg=v=>setSel(s=>({...s,veggies:s.veggies.includes(v)?s.veggies.filter(x=>x!==v):[...s.veggies,v]}));
  return(<div style={{padding:20}}><h2>Build Your Pizza</h2>
  <p>Step 1 — Base:</p>{opt.bases.map(b=><button key={b} onClick={()=>pick('base',b)}>{b}</button>)}
  <p>Step 2 — Sauce:</p>{opt.sauces.map(s=><button key={s} onClick={()=>pick('sauce',s)}>{s}</button>)}
  <p>Step 3 — Cheese:</p>{opt.cheeses.map(c=><button key={c} onClick={()=>pick('cheese',c)}>{c}</button>)}
  <p>Step 4 — Veggies (multi):</p>{opt.veggies.map(v=><label key={v}><input type="checkbox" onChange={()=>toggleVeg(v)}/>{v} </label>)}
  <h3>Summary</h3><pre>{JSON.stringify(sel,null,2)}</pre>
  <button onClick={order}>Pay ₹299 (Razorpay test) & Order</button></div>);
}
