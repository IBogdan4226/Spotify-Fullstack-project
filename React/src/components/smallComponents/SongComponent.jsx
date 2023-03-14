import React from 'react'
import song from "./../../resources/music.png"
import single from "./../../resources/single.jpg"
import album from "./../../resources/album.jpg"
import { Link } from 'react-router-dom'
export const SongComponent = ({ id, name, genre, year, type, parent }) => {
    const choosePicture = () => {
        switch (type) {
            case "single":
                return single;

            case "album":
                return album;

            case "song":
                return song;

            default:
                return song;
        }
    }
    return (<div>{type != 'album' && <div style={{ display: "flex", width: "150px", flexDirection: "column", justifyContent: "center" }}>
        <div style={{ width: "150px", aspectRatio: "1/1", overflow: "hidden" }}>
            <img src={choosePicture()} alt="music" style={{ width: "100%", height: "100%", overflow: "hidden" }} />
        </div>
        <p style={{ fontSize: "1.35rem", fontWeight: "800", textTransform: "capitalize" }}>{name}</p>
        <p>Genre: <b style={{ textTransform: "capitalize" }}>{genre}</b>----{year}</p>
    </div>}

        {type == 'album' &&
            <Link
                to="/album/songs" state={{ parentId: id ,albumName:name}}
            >
                <div style={{ display: "flex", width: "150px", flexDirection: "column", justifyContent: "center" }}>
                    <div style={{ width: "150px", aspectRatio: "1/1", overflow: "hidden" }}>
                        <img src={choosePicture()} alt="music" style={{ width: "100%", height: "100%", overflow: "hidden" }} />
                    </div>
                    <p style={{ fontSize: "1.35rem", fontWeight: "800", textTransform: "capitalize" }}>{name}</p>
                    <p>Genre: <b style={{ textTransform: "capitalize" }}>{genre}</b>----{year}</p>
                </div></Link>}
    </div>

    )
}
