import React, { useEffect, useState } from 'react'
import { SongComponent } from './smallComponents/SongComponent'
import InfiniteScroll from "react-infinite-scroll-component";
export const SongsPage = ({ currUser, setCurrUser }) => {
    const [data, setData] = useState([]);
    const [err, setErr] = useState("");
    const [errAdd, setErrAdd] = useState("");
    const [succAdd,setSuccAdd]= useState("");
    const [page, setPage] = useState(0);
    const [hasMore, setMore] = useState(true)
    const [needAlbum, SetNeedAlbum] = useState(true);
    const fetchData = () => {
        fetch(process.env.REACT_APP_SONGCOLLECTION_REST + `/songs?page=${page}&pageSize=5`)
            .then((response) => {
                if (!response.ok || !response.status === 204) {
                    throw new Error(`This is an HTTP error: The status is ${response.status}`)
                }
                if (response.status === 204) {
                    setMore(false)
                    return [];
                }
                return response.json();

            })
            .then((response) => {
                // debugger;
                if (response.length < 1) {
                    return response;
                }
                return response._embedded?.songList
            })
            .then((newData) => {

                console.log(newData);
                setPage(page + 1);
                setData([...data, ...newData]);
            })
            .catch((err) => {
                console.log(err);
                setErr("Error trying to get songs")
            })
    }
    const fetchArtist = () => {
        fetch(process.env.REACT_APP_SONGCOLLECTION_REST + `/artists`)
            .then((response) => {
                if (!response.ok||!response.status==204) {
                    setErrAdd("SongCollection service is down")
                    return;
                }
                return response.json();
            })
            .then((response) => {
                return response._embedded?.artistList
            })
            .then((dataArtist) => {
                const artistSelect = document.querySelector("#addSongArtist");
                dataArtist.forEach((artist) => {
                    var myNewOption = new Option(artist.name, artist.uuid);
                    artistSelect.add(myNewOption);
                })
            })
            .catch((err) => {
                setErrAdd("SongCollection service is down")
                return;
            })
    }
    const fetchAlbums = () => {
        fetch(process.env.REACT_APP_SONGCOLLECTION_REST + `/songs?type=album`)
           
            .then((response) => {
                if (!response.ok&&!response.status==204) {
                    setErrAdd("SongCollection service is down")
                    return;
                }
                if(response.status==204){
                    return [];
                }
                return response.json();
            })
            .then((response) => {
                return response._embedded?.songList
            })
            .then((dataAlbums) => {
                const albumSelect = document.querySelector("#addSongParent");
                
                dataAlbums?.forEach((album) => {

                    var myNewOption = new Option(album.name, album.id);
                    albumSelect.add(myNewOption);
                })
            })
            .catch((err) => {
                console.log(err);
                setErrAdd("SongCollection service is down")
                return;
            })
    }
    const refresh = (setData) => { };
    useEffect(() => {
        fetchData();
        if (currUser.isAuth) {
            fetchAlbums();
        }
        if (currUser.roleId === 4) {
            fetchArtist();
        }
    }, [])


    const addSong = () => {
        setErr("");
        setSuccAdd("");
        const songName = document.querySelector("#addSongName").value;
        const songGenre = document.querySelector("#addSongGenre").value;
        const songYear = document.querySelector("#addSongYear").value.toString();
        const songType = document.querySelector("#addSongType").value;
        let songParent;
        if (songType === "song") {
            songParent = document.querySelector("#addSongParent").value;

        } else {
            songParent = null;
        }
        const jsonBody = {
            name: songName,
            genre: songGenre,
            year: songYear,
            type: songType,
            parent: songParent
        }
        fetch(process.env.REACT_APP_SONGCOLLECTION_REST + `/songs`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + currUser.token,
            },
            body: JSON.stringify(jsonBody)
        })
            .then((response) => {

                if (response.status === 401) {
                    setErrAdd("Invalid token. Nu mai adauga melodii golane")
                    return;
                }
                return response.json();

            })
            .then((response) => {
                const songId = response.id;
                const artistSong = {
                    uuid: currUser.roleId === 4 ? document.querySelector("#addSongArtist").value : currUser.userId,
                    id: songId
                }
                fetch(process.env.REACT_APP_SONGCOLLECTION_REST + `/artist/song`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': 'Bearer ' + currUser.token,
                    },
                    body: JSON.stringify(artistSong)
                }).then((response) => {
                    if (response.status !== 201) {
                        setErrAdd("Eroare la adaugarea melodiei.")
                        fetch(process.env.REACT_APP_SONGCOLLECTION_REST + `/songs/` + songId, {
                            method: 'DELETE',
                            headers: {
                                'Content-Type': 'application/json',
                                'Authorization': 'Bearer ' + currUser.token,
                            }
                        })
                        return;
                    }
                    else{
                        setSuccAdd("Song added successfully")
                        setData([...data,jsonBody])
                    }
                })

            })
            .catch((err) => {
                console.log(err);
                setErr("Error at trying to add the song. Try again.")
            })
    }

    return (
        <div style={{ display: "grid", gridTemplateColumns: "1fr 0.3fr" }}>
            <div>
                <h2 style={{ color: "red", textAlign: "center" }}>{err}</h2>
                {data && <InfiniteScroll
                    dataLength={data.length}
                    next={() => {
                        fetchData();
                    }}
                    hasMore={hasMore}
                    loader={<h4>Loading...</h4>}
                    endMessage={
                        <p style={{ textAlign: "center" }}>
                            <b>Yay! You have seen it all</b>
                        </p>
                    }

                    refreshFunction={refresh}
                    pullDownToRefresh
                    pullDownToRefreshThreshold={50}
                    pullDownToRefreshContent={
                        <h3 style={{ textAlign: "center" }}># 8595; Pull down to refresh</h3>
                    }
                    releaseToRefreshContent={
                        <h3 style={{ textAlign: "center" }}># 8593; Release to refresh</h3>
                    }
                >
                    <div style={{ minHeight: "100vh", display: "flex", alignItems: "center", flexDirection: "column" }}>
                        {data.map((song, index) => (
                            <SongComponent key={index} {...song} />
                        ))}
                    </div>
                </InfiniteScroll>}
            </div>
            {currUser.roleId > 1 &&
                <div style={{ display: "flex", justifyContent: "center", flexDirection: "column", height: "calc(100vh - 32px)", position: "sticky", top: "0" }}>
                    <h3 style={{ textAlign: "center" }}>Insert song</h3>
                    <input type="text" id="addSongName" placeholder='Song name' />
                    <select id="addSongGenre" name="genre">
                        <option value="rap">Rap</option>
                        <option value="pop">Pop</option>
                        <option value="Trap">Trap</option>
                    </select>
                    <input type="tel" id="addSongYear" placeholder='Year' maxLength="4" />
                    <select id="addSongType" name="type" onChange={() => {
                        const val = document.querySelector("#addSongType").value;
                        if (val === "song") {
                            SetNeedAlbum(true);
                        }
                        else {
                            SetNeedAlbum(false);
                        }
                    }}>
                        <option value="song">Song</option>
                        <option value="single">Single</option>
                        <option value="album">Album</option>
                    </select>
                    <select id="addSongParent" name="albumParent" style={needAlbum ? { display: "block" } : { display: "none" }}></select>
                    {currUser.roleId === 4 && <select id="addSongArtist" name="songArtist" ></select>}
                    <button type="button" onClick={() => addSong()}>Add Entry</button>
                    <h2 style={{ color: "red", textAlign: "center" }}>{errAdd}</h2>
                    <h2 style={{ color: "green", textAlign: "center" }}>{succAdd}</h2>
                </div>}
        </div>
    )
}
