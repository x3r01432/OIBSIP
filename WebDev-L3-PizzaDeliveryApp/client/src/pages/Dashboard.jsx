import {useEffect,useState} from 'react';import api from '../api';
export default function Dashboard(){
  const[v,setV]=useState([]);const[orders,setOrders]=useState([]);
  useEffect(()=>{api.get('/pizza').then(r=>setV(r.data.varieties)).catch(()=>{});
    api.get('/orders/mine').then(r=>setOrders(r.data)).catch(()=>{});
    const i=setInterval(()=>api.get('/orders/mine').then(r=>setOrders(r.data)).catch(()=>{}),5000);
    return()=>clearInterval(i);},[]);
  return(<div style={{padding:20}}><h2>Available Pizzas</h2><ul>{v.map(x=><li key={x}>{x}</li>)}</ul>
  <h3>My Orders (live status, polled every 5s)</h3>
  {orders.map(o=><div key={o._id}>{o.base} — <b>{o.status}</b></div>)}</div>);
}
