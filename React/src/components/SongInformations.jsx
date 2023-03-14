import React, { useEffect, useState } from 'react'
import { useLocation } from "react-router";
import { SongComponent } from './smallComponents/SongComponent';
export const SongInformations = () => {
    const id = useLocation().state.songId;
    const [data, setData] = useState({});
    const [err, setErr] = useState("");
    const fetchData = () => {
        fetch(process.env.REACT_APP_SONGCOLLECTION_REST + `/songs/${id}`)
            .then((response) => {
                if (!response.ok) {
                    throw new Error(`This is an HTTP error: The status is ${response.status}`)
                }

                return response.json();

            })

            .then((newData) => {
                console.log(newData);
                setData(newData);
            })
            .catch((err) => {
                console.log(err);
                setErr("Error trying to get songs")
            })
    }
    useEffect(() => {
        fetchData();
    }, [])
    return (

        <div style={{display:"flex",alignItems:"center", justifyContent:"center",height:"100%"}}>
            <p style={{ color: "red", fontWeight: "800" }}>{err}</p>
            <SongComponent {...data}/>
        </div>
    )
}
