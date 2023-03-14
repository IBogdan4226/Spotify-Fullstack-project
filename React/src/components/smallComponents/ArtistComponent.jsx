import React from 'react'
import artist from "./../../resources/artist.png"
import { Link } from 'react-router-dom'
export const ArtistComponent = ({uuid, name, active }) => {
  return (
    <Link
    to="/artist/songs" state={{ artistId:uuid ,artistName:name}}
    >
      <div style={{ display: "flex", width: "150px", flexDirection: "column", justifyContent: "center" }}>
        <div style={{ width: "150px", aspectRatio: "1/1", overflow: "hidden" }}>
          <img src={artist} alt="artist" style={{ width: "100%", height: "100%", overflow: "hidden" }} />
        </div>
        <p style={{ fontSize: "1.35rem", fontWeight: "800", textTransform: "capitalize" }}>{name}</p>
        <p>Active: <b style={{ textTransform: "capitalize" }}>{active ? "True" : "False"}</b></p>
      </div>
    </Link>

  )
}
