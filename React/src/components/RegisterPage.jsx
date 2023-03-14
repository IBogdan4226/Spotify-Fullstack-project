import React, { useEffect, useState } from 'react'
import { createAccount } from '../soapUtils/soapUtils';
import { useNavigate } from "react-router-dom";

export const RegisterPage = ({ currUser,setCurrUser}) => {
    const [error, changeError] = useState("")
    const [user, changeUser] = useState("")
    const [confirm, changeConfirm] = useState("")
    const [password, changePassword] = useState("")
    const [Cpassword, changeCPassword] = useState("")
    const navigate = useNavigate();
    useEffect(() => {
        if (currUser.isAuth) {
          navigate("/")
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
      }, [])
    const handleSubmit=(e)=>{
        e.preventDefault();
        changeError("")
        if(user.length<3){
            changeError("Please make sure your username is valid!")
            return;
        }
        if(password!==Cpassword){
            changeError("Please make sure your password is correct!")
            return;
        }
        try{
            createAccount(user,password,(output)=>{
                if(output===false){
                    changeError("Name not available!")
                    return;
                }
                changeConfirm("Acount created")
                // setTimeout(()=>{
                //     navigate("/../login")
                // },3000)
            })


        }
        catch(err){
            changeError("Error while creating account. Please try again later!")
            return;
        }


    }
    const onchangeUser = (e) => {
        changeUser(() => e.target.value)
    }
    const onchangePassword = (e) => {
        changePassword(() => e.target.value)
    }

    const onchangeCPassword = (e) => {
        changeCPassword(() => e.target.value)
    }
   
    return (
        <div style={{ height: "100%", width: "100vw", display: "flex", flexDirection: "column", alignItems: "center", justifyContent: "center" }}>
            <form onSubmit={handleSubmit} style={{ display: "flex", flexDirection: "column", alignItems: "center" }}>
                <input type="text" placeholder='User' style={{ marginBottom: "25px" }} onChange={onchangeUser} />
                <input type="password" placeholder='Password' style={{ marginBottom: "25px" }} onChange={onchangePassword} />
                <input type="password" placeholder='Confirm password' style={{ marginBottom: "25px" }} onChange={onchangeCPassword} />
                <button type='submit'>Register</button>
            </form>
            <p style={{ color: "red", fontWeight: "800" }}>{error}</p>
            <p style={{ color: "green", fontWeight: "800" }}>{confirm}</p>
            
        </div>
    )
}
