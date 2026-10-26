import {useEffect,useState} from 'react';import api from '../api';
export default function AdminInventory(){
  const[inv,setInv]=useState([]);
  const load=()=>api.get('/inventory').then(r=>setInv(r.data)).catch(()=>{});
  useEffect(load,[]);
  const upd=(id,stock)=>api.put('/inventory/'+id,{stock:+stock}).then(load);
  return(<div style={{padding:20}}><h2>Inventory</h2>
  {inv.map(i=><div key={i._id}>{i.type} {i.name}: {i.stock}
   <input defaultValue={i.stock} onBlur={e=>upd(i._id,e.target.value)} style={{width:60,marginLeft:8}}/></div>)}</div>);
}
