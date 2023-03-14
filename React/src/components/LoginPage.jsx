import React, { useEffect, useState } from 'react'
import { login, authorize } from '../soapUtils/soapUtils';
import { useNavigate } from "react-router-dom";
import { NavLink } from "react-router-dom";

export const LoginPage = ({ currUser, setCurrUser }) => {
  const navigate = useNavigate();

  useEffect(() => {
    if (currUser.isAuth) {
      navigate("/")
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [])

  const handleSubmit = (e) => {
    e.preventDefault();
    changeError("");
    try {
      login(user, password, (token) => {
        if (token.includes("match")) {
          changeError("User or password is invalid");
        }
        else {
          try {
            authorize(token, (output) => {
              if (output === "") {
                changeError("Error at trying to validate user priviledges.");
                return;
              }
              const [userId, roleId] = output.split("-");
              setCurrUser((prev) => {
              
                return {
                  isAuth: true,
                  userName:user,
                  userId: parseInt(userId),
                  roleId: parseInt(roleId),
                  token: token
                }
              })
              navigate("/")
            })
          }
          catch (err) {
            changeError("Error at trying to validate user priviledges.");
          }

        }
      })

    }
    catch (err) {
      changeError("User or password is invalid");
    }


  }
  const onchangeUser = (e) => {
    changeUser(() => e.target.value)
  }
  const onchangePassword = (e) => {
    changePassword(() => e.target.value)
  }
  const [error, changeError] = useState("")
  const [user, changeUser] = useState("")
  const [password, changePassword] = useState("")
  return (
    <div style={{ height: "100%", width: "100vw", display: "flex", flexDirection: "column", alignItems: "center", justifyContent: "center" }}>
      <form onSubmit={handleSubmit} style={{ display: "flex", flexDirection: "column", alignItems: "center" }}>
        <input type="text" placeholder='User' style={{ marginBottom: "25px" }} onChange={onchangeUser} />
        <input type="password" placeholder='Password' style={{ marginBottom: "25px" }} onChange={onchangePassword} />
        <button type='submit'>Login</button>
      </form>
      <p style={{ color: "red", fontWeight: "800" }}>{error}</p>
      <NavLink to="/../register"
        style={{ textDecoration: "none" }}
      >
        Don't have an a account? Register here.
      </NavLink>
    </div>
  )
}


