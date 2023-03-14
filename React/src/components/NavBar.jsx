import React from 'react'
import { NavLink } from "react-router-dom";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faUser } from '@fortawesome/free-solid-svg-icons'
import { faArrowRightFromBracket } from '@fortawesome/free-solid-svg-icons';
export const NavBar = ({ currUser }) => {
    let activeStyle = {
        textDecoration: "underline",
    };

    return (

        <nav style={styles.navMenu}>
            <NavLink to="/"
                style={({ isActive }) =>
                    isActive ? activeStyle : { textDecoration: "none" }}
            >
                Home
            </NavLink>
            <NavLink to="songs"
                style={({ isActive }) =>
                    isActive ? activeStyle : { textDecoration: "none" }}
            >
                All Songs
            </NavLink>

            <NavLink to="artists"
                style={({ isActive }) =>
                    isActive ? activeStyle : { textDecoration: "none" }
                }
            >
                All Artists
            </NavLink>
            {!currUser.isAuth ?
                <NavLink to="login"
                    style={({ isActive }) =>
                        isActive ? activeStyle : { textDecoration: "none" }
                    }
                >
                    <FontAwesomeIcon icon={faUser} />
                </NavLink> :
                <NavLink to="logout"
                    style={({ isActive }) =>
                        isActive ? activeStyle : { textDecoration: "none" }
                    }
                >
                    <FontAwesomeIcon icon={faArrowRightFromBracket} />
                </NavLink>
            }
        </nav>
    )
}

const styles = {
    navMenu: {
        display: "flex",
        justifyContent: "space-between",
        fontSize: "1.5rem",
        background: "gray",
        color: "black"
    }
}
