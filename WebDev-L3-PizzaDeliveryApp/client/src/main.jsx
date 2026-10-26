import React from 'react';import {createRoot} from 'react-dom/client';
import {BrowserRouter,Routes,Route,Link} from 'react-router-dom';
import Login from './pages/Login';import Register from './pages/Register';
import Dashboard from './pages/Dashboard';import Builder from './pages/Builder';
import AdminInventory from './pages/AdminInventory';import AdminOrders from './pages/AdminOrders';
function App(){return(<BrowserRouter>
<nav style={{padding:10,background:'#b91c1c',color:'#fff'}}>
<Link to="/" style={{color:'#fff',marginRight:10}}>Home</Link>
<Link to="/builder" style={{color:'#fff',marginRight:10}}>Build Pizza</Link>
<Link to="/admin/inventory" style={{color:'#fff',marginRight:10}}>Admin Inventory</Link>
<Link to="/admin/orders" style={{color:'#fff'}}>Admin Orders</Link></nav>
<Routes>
<Route path="/" element={<Dashboard/>}/>
<Route path="/login" element={<Login/>}/>
<Route path="/register" element={<Register/>}/>
<Route path="/builder" element={<Builder/>}/>
<Route path="/admin/inventory" element={<AdminInventory/>}/>
<Route path="/admin/orders" element={<AdminOrders/>}/>
</Routes></BrowserRouter>);}
createRoot(document.getElementById('root')).render(<App/>);
