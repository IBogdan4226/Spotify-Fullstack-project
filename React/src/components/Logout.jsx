import React, { useEffect } from 'react'
import { useNavigate } from "react-router-dom";
import { logout } from '../soapUtils/soapUtils';
export const Logout = ({ currUser, setCurrUser }) => {
    const navigate = useNavigate();
    useEffect(() => {
        if(!currUser.isAuth){
            navigate("/")
            return;
        }
        try {
            logout(currUser.token, () => {
                setCurrUser((prev) => {
                    return {
                        isAuth:false,
                        userName:"",
                        userId:-1,
                        roleId:-1,
                        token:"",
                    }
                })

            })
        }
        catch (err) {
            document.querySelector("#logout").innerHTML = "Logout failed. Try again later."
        }
// eslint-disable-next-line react-hooks/exhaustive-deps
    }, [])
    return (
        <div id="logout">You've been Logout.</div>
    )
}
