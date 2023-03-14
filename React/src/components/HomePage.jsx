import React, { useEffect, useState } from 'react'
import { getUsers, makeClientArtist } from '../soapUtils/soapUtils';
import { PlaylistComponent } from './smallComponents/PlaylistComponent';
export const HomePage = ({ currUser, setCurrUser }) => {
  const [data, setData] = useState([]);
  const [dataPlaylist, setDataPlaylist] = useState([]);
  const [err, setErr] = useState("");
  const [errChange, setErrChange] = useState("");
  const [succAdd, setSuccAdd] = useState("");
  document.addEventListener("deleteRefresh", () => {
    fetchPlaylist();
  })



  const fetchData = () => {
    getUsers(currUser.token, (output) => {
      if (output === "") {
        setErr("Token invalid")
        return;
      }
      setData(output.split(/\r?\n/).slice(0, -1));
      console.log(output.split(/\r?\n/).slice(0, -1));
    })
  }



  const fetchPlaylist = () => {
    fetch(process.env.REACT_APP_PLAYLIST_REST)
      .then((response) => {
        if (!response.ok) {
          throw new Error(`This is an HTTP error: The status is ${response.status}`)
        }
        return response.json();

      })
      .then((response) => {
        return response._embedded?.playlistDTOList
      })
      .then((newData) => {
        const selectPlaylist = document.querySelector("#playlistNameSelect");
        while (selectPlaylist.options.length > 0) {
          selectPlaylist.remove(0);
        }
        let myPlaylists = [];
        newData?.forEach((playlist) => {
          if (playlist.creatorId === currUser.userId) {
            myPlaylists.push(playlist);
            let myNewOption = new Option((playlist.playlistName + playlist.id.slice(-4)), playlist.id);
            selectPlaylist.add(myNewOption);
          }
        })
        setDataPlaylist(myPlaylists);
      })
      .catch((err) => {
        console.log(err);
        setErr("Error trying to get playlist")
      })
  }


  const fetchSongs = () => {
    fetch(process.env.REACT_APP_SONGCOLLECTION_REST + `/songs?pageSize=100000`)
      .then((response) => {
        if (!response.ok) {
          throw new Error(`This is an HTTP error: The status is ${response.status}`)
        }
        return response.json();
      })
      .then((response) => {
        return response._embedded?.songList
      })
      .then((newData) => {
        const selectSongs = document.querySelector("#songToAddSelect");
        newData?.forEach((song) => {
          let myNewOption = new Option(song.name, song.id);
          selectSongs.add(myNewOption);
        })

      })
      .catch((err) => {
        console.log(err);
      })
  }


  const changeRole = () => {
    setErrChange("");
    setSuccAdd("");
    const userId = document.querySelector("#userId").value;
    let userExist = false;
    data.map((user) => {
      const [id, name, role] = user.trim().split('-');
      if (id.trim() === userId) {
        userExist = true;
      }
    })
    if (!userExist) {
      setErrChange("User id invalid!!!!!")
      return;
    }
    if (!document.querySelector("#isArtist").checked) {
      setErrChange("Please confirm change!!!!")
      return;
    }
    console.log(data[1]);
    const artistName=(data.filter((data)=>data.split('-')[0].trim()==userId)).toString().split("-")[1].trim();
    console.log(artistName);
    const artist={
      'name': artistName,
      'active': true
    }
    

    makeClientArtist(userId, currUser.token, (success) => {
      if (success) {
        fetch(process.env.REACT_APP_SONGCOLLECTION_REST + "/artists/" + userId, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + currUser.token,
          },
          body: JSON.stringify(artist)
        }).then((response) => {
          if (response.status === 204 || response.status === 201) {
            setSuccAdd("Artist creat");
            fetchData();
          }
          else {
            setErrChange("Eroare la schimbarea rolului")
          }
        })
          .catch((err) => {
            setErrChange("Eroare la schimbarea rolului")
          })
      }
      else {
        setErrChange("Eroare la schimbarea rolului")
      }
    })

  }



  const createPlaylist = () => {
    setErr("")
    let namePlaylist = document.querySelector("#playlistName").value;
    if (namePlaylist.length === 0) {
      namePlaylist = "untitled";
    }
    const isVisible = document.querySelector("#isVisible").checked;

    const bodyJson = {
      creatorId: currUser.userId,
      playlistName: namePlaylist,
      visible: isVisible
    }
    fetch(process.env.REACT_APP_PLAYLIST_REST, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + currUser.token,
      },
      body: JSON.stringify(bodyJson)
    }).then((response) => {
      if (response.status === 201) {
        fetchPlaylist();
      }
      else {
        setErr("Failed to add Playlist")
      }
    })
      .catch((err) => {
        setErr("Failed to add Playlist")
      })
  }


  const addToPlaylist = () => {
    const playlistId = document.querySelector("#playlistNameSelect").value;
    const songId = [document.querySelector("#songToAddSelect").value];

    console.log(playlistId, songId);

    fetch(process.env.REACT_APP_PLAYLIST_REST + "/append/" + playlistId, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + currUser.token,
      },
      body: JSON.stringify({ songs: songId })
    }).then((response) => {
      if (response.status === 200) {
        fetchPlaylist();
      }
      else {
        setErr("Failed to add to Playlist")
      }
    })
      .catch((err) => {
        setErr("Failed to add to Playlist")
      })

  }


  useEffect(() => {
    if (currUser.roleId === 4) {
      fetchData();
    }
    else {
      if (currUser.isAuth) {
        fetchSongs();
        fetchPlaylist();
      }
    }
  }, [])



  return (

    <div>

      <h1 style={{ textAlign: "center" }}>HOME PAGE</h1>
      {currUser.roleId === 4 &&
        <div style={{ display: "grid", gridTemplateColumns: "1fr 0.3fr" }}>
          <div>
            <p style={{ color: "red", fontWeight: "800" }}>{err}</p>
            {data &&
              <div>
                <table>
                  <tr>
                    <th>ID</th>
                    <th>NAME</th>
                    <th>CLIENT</th>
                    <th>ARTIST</th>
                  </tr>
                  {data.map((user, index) => {
                    const [id, name, role] = user.split('-');
                    const roles = role.trim().split('*');

                    return !roles.includes("4") && <tr>
                      <td>{id}</td>
                      <td>{name}</td>
                      <td>{roles.includes('1') ? 'True' : 'False'}</td>
                      <td>{roles.includes('2') ? 'True' : 'False'}</td>
                    </tr>;
                  })}

                </table>
              </div>
            }
          </div>

          <div style={{ display: "flex", flexDirection: "column", height: "calc(100vh - 100px)", position: "sticky", top: "0" }}>
            <h3>Change user to artist</h3>
            <input type="text" id="userId" placeholder='USER ID' />
            <div>
              <label style={{ marginRight: "15px" }}>Confirm</label>
              <input type="checkbox" id="isArtist" />
            </div>
            <button type='button' onClick={changeRole}>Make change</button>
            <p style={{ color: "red", fontWeight: "800" }}>{errChange}</p>
            <p style={{ color: "green", fontWeight: "800" }}>{succAdd}</p>
          </div>

        </div>
      }

      {currUser.roleId !== 4 && currUser.isAuth &&
        <div>
          <p style={{ color: "red", fontWeight: "800" }}>{err}</p>
          <div style={{ display: "grid", gridTemplateColumns: "1fr 0.3fr" }}>
            <div>
              {dataPlaylist &&
                dataPlaylist.map((playlist, index) => {
                  return <PlaylistComponent key={index} token={currUser.token} {...playlist} />
                })
              }
            </div>
            <div>
              <div style={{ display: "flex", flexDirection: "column", marginBottom: "30px" }}>
                <input type="text" id="playlistName" placeholder='Playlist name' />
                <div>
                  <label style={{ marginRight: "10px" }}>Visible to others: </label>
                  <input type="checkbox" id="isVisible" />
                </div>
                <button type='button' onClick={createPlaylist}>Create playlist</button>
              </div>

              <div style={{ display: "flex", flexDirection: "column", marginBottom: "30px" }}>
                <select name="playlistName" id="playlistNameSelect"></select>
                <select name="songToAdd" id="songToAddSelect"></select>
                <button type='button' onClick={addToPlaylist}>Add song to Playlist</button>

              </div>



            </div>
          </div>
        </div>}

    </div>
  )
}
