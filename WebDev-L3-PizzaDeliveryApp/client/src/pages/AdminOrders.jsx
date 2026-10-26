import {useEffect,useState} from 'react';import api from '../api';
const STAGES=['Order Received','In Kitchen','Sent to Delivery'];
export default function AdminOrders(){
  const[orders,setOrders]=useState([]);
  const load=()=>api.get('/orders/all').then(r=>setOrders(r.data)).catch(()=>{});
  useEffect(load,[]);
  const setStatus=(id,status)=>api.put(`/orders/${id}/status`,{status}).then(load);
  return(<div style={{padding:20}}><h2>All Orders</h2>
  {orders.map(o=><div key={o._id}>{o.base} — {o.status}
   {STAGES.map(s=><button key={s} onClick={()=>setStatus(o._id,s)}>{s}</button>)}</div>)}</div>);
}
