import {useState} from 'react';import api from '../api';
export default function Register(){
  const[f,setF]=useState({name:'',email:'',password:''});const[m,setM]=useState('');
  const submit=async e=>{e.preventDefault();try{const{data}=await api.post('/auth/register',f);setM(data.msg);}
    catch(err){setM(err.response?.data?.msg||'Error');}};
  return(<form onSubmit={submit} style={{padding:20}}><h2>Register</h2>
  <input placeholder="Name" onChange={e=>setF({...f,name:e.target.value})}/><br/>
  <input placeholder="Email" onChange={e=>setF({...f,email:e.target.value})}/><br/>
  <input type="password" placeholder="Password" onChange={e=>setF({...f,password:e.target.value})}/><br/>
  <button>Register</button><p>{m}</p></form>);
}
