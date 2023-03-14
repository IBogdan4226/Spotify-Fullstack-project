import React, { useEffect, useState } from 'react'
import InfiniteScroll from "react-infinite-scroll-component";
import { ArtistComponent } from './smallComponents/ArtistComponent';
export const ArtistPage = () => {
    const [data, setData] = useState([]);
    const [err, setErr] = useState("");
    const [page,setPage]=useState(0);
    const [hasMore,setMore]=useState(true)
    const fetchData = () => {
        
        fetch(process.env.REACT_APP_SONGCOLLECTION_REST + `/artists?page=${page}&pageSize=5`)
            .then((response) => {
                if (!response.ok || !response.status==204) {
                    throw new Error(`This is an HTTP error: The status is ${response.status}`)
                }
                if(response.status==204){
                    setMore(false)
                    return [];
                }
                return response.json();

            })
            .then((response)=>{
                // debugger;
                if(response.length<1){
                    return response;
                }
                return response._embedded?.artistList
            })
            .then((newData) => {
                
                console.log(newData);
                setPage(page+1);
                setData([...data, ...newData]);
            })
            .catch((err) => {
                console.log(err);
                setErr("Error trying to get songs")
            })
    }
    const refresh = (setData) => { };
    useEffect(() => {
        fetchData();
    }, [])


    return (
        
        <div>
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
                <div style={{ minHeight: "100vh", display:"flex",alignItems:"center",flexDirection:"column"}}>
                    {data.map((artist, index) => (
                        <ArtistComponent key={index} {...artist} />
                    ))}
                </div>
            </InfiniteScroll>}
        </div>

    )
}
