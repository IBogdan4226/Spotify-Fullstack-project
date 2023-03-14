import React from 'react'
import { Link } from 'react-router-dom'

export const PlaylistComponent = ({ token, id, playlistName, songs }) => {
    const deletePlaylist = () => {
        fetch(process.env.REACT_APP_PLAYLIST_REST + "/" + id, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token,
            }
        })
        const myEvent = new CustomEvent("deleteRefresh", {
            bubbles: true,
        });
        document.dispatchEvent(myEvent);
    }
    return (
        <div style={{ border: "1px black solid" }}>
            <h3>{playlistName+"-"+id.slice(-4)}</h3>
            <div>
                <h4>Songs:</h4>
                <div style={{ columnCount: "3" }}>
                    {songs?.map((song, index) => {
                        return <Link
                            to="/song-details" state={{ songId: song.id }}
                            key={index}
                        >
                            <p>{song.name}</p>
                        </Link>
                    })}
                </div>

            </div>
            <button type='button' onClick={deletePlaylist}>Delete</button>

        </div>
    )
}
