import { Route, Routes } from "react-router-dom";
import { HomePage } from "./components/HomePage";
import { LoginPage } from "./components/LoginPage";
import { RegisterPage } from "./components/RegisterPage";
import { NavBar } from "./components/NavBar";
import { useState } from "react";
import { Logout } from "./components/Logout";
import { SongsPage } from "./components/SongsPage";
import { ArtistPage } from "./components/ArtistPage";
import { ArtistSongsPage } from "./components/ArtistSongsPage";
import { SongInformations } from "./components/SongInformations";
import { AlbumSongs } from "./components/AlbumSongs";
function App() {

  const [authUser,setAuthUser]=useState(user)
  return (
    <div style={{height:"100vh"}}>
      <NavBar currUser={authUser}/>
      <div style={{height:"calc(100vh - 32px) "}}>
      <Routes>
        <Route path="">
          <Route index element={<HomePage currUser={authUser} setCurrUser={setAuthUser}/>}></Route>
          <Route exact path="/login" element={<LoginPage currUser={authUser} setCurrUser={setAuthUser} />} />
          <Route exact path="/register" element={<RegisterPage currUser={authUser}  setCurrUser={setAuthUser}/>} />
          <Route exact path="/logout" element={<Logout currUser={authUser}  setCurrUser={setAuthUser}/>} />
          <Route exact path="/songs" element={<SongsPage currUser={authUser}  setCurrUser={setAuthUser}/>} />
          <Route exact path="/artists" element={<ArtistPage currUser={authUser}  setCurrUser={setAuthUser}/>} />
          <Route exact path="/artist/songs" element={<ArtistSongsPage />} />
          <Route exact path="/song-details" element={<SongInformations/>} />
          <Route exact path="/album/songs" element={<AlbumSongs/>} />
        </Route>
      </Routes>
      </div>
     
    </div>
  );
}

const user={
  isAuth:false,
  userName:"",
  userId:-1,
  roleId:-1,
  token:"",
}
export default App;
