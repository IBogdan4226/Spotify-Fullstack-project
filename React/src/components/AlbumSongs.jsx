import React, { useEffect, useState } from 'react'
import { useLocation } from "react-router";
import { SongComponent } from './smallComponents/SongComponent';
import InfiniteScroll from "react-infinite-scroll-component";
export const AlbumSongs = () => {
    const parentID = useLocation().state.parentId;
    const albumName=useLocation().state.albumName;
    const [data, setData] = useState([]);
    const [err, setErr] = useState("");
    useEffect(() => {
        fetchData();
    }, [])
    const refresh = (setData) => { };
    const fetchData = () => {
        fetch(process.env.REACT_APP_SONGCOLLECTION_REST + `/songs?parent=${parentID}`)
            .then((response) => {
                if (!response.ok || !response.status === 204) {
                    throw new Error(`This is an HTTP error: The status is ${response.status}`)
                }
                if (response.status === 204) {
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
                setData([...data, ...newData]);
            })
            .catch((err) => {
                console.log(err);
                setErr("Error trying to get songs")
            })
    }
  return (
    <div>
        <h2 style={{ color: "red", textAlign: "center" }}>{err}</h2>
        <div style={{position:"absolute",left:"50px",top:"50px",width:"200px",fontSize:"16px",fontWeight:"800"}}>Vedeti melodiile din albumul :{albumName}</div>
    {data && <InfiniteScroll
        dataLength={data.length}
        next={() => {
            fetchData();
        }}
        hasMore={false}
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
  )
}
