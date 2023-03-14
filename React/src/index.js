import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import 'jquery';
import 'jquery.soap';
import { BrowserRouter } from "react-router-dom";

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  // <React.StrictMode>
    <BrowserRouter>
    <App />
    </BrowserRouter>
  
  // </React.StrictMode>
  //In strict mode, useEffect is called twice in development mode
);


