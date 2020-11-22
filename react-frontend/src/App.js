import logo from './logo.svg';
import './styles/views/main-page.scss';
import { useEffect, useState } from 'react'
import Table from './components/blocks/Table'
import SideBar from './components/blocks/SideBar'
import ActionBlock from './components/atoms/ActionBlock'
import SearchBar from './components/atoms/SearchBar'
import API from './helpers/Api';

function App() {

  const [ data, setData ] = useState([])
  const [ isConnected, setIsConnected ] = useState(false)
  const [ socket, setSocket ] = useState()

  const [ dirs, setDirs ] = useState([])
  const [ newFile, setNewFile ] = useState()

  useEffect(() => {
    console.log(process.env)
    if (!isConnected) {
      const socket = new WebSocket("ws://localhost:8080/cloud_websocket")

      socket.onopen = () => {
        socket.send(JSON.stringify({
            type: "initialConn",
        }));
        setIsConnected(true)
      };

      socket.onmessage = m => {
        setData(JSON.parse(m.data))
      };
      setSocket(socket)
    }
  }, [])

  function navigateToDir(dirName) {
    let newDirStack = [
      ...dirs,
      dirName
    ]
    const tmp = [...dirs]
    setDirs(newDirStack)
    API.changeDir(socket, isConnected, newDirStack, tmp)
  }

  function goBack() {
    let dirStack = [...dirs]
    const tmp = [...dirs]

    dirStack.pop()
    setDirs(dirStack)
    API.changeDir(socket, isConnected, dirStack, tmp)
  }

  const deleteFile = async (fileName) => {
    const fileDirToDelete = `${dirs.join('/')}/${fileName}`
    let res = await API.deleteFile(fileDirToDelete)
  }

  function submitNewFile(e){
      e.preventDefault()

      let reqBody = new FormData()
      reqBody.append("file", newFile)
      reqBody.append("dir", './' + dirs.join('/'))

      API.submitNewFile(reqBody)
  }

  
  return (
    <div className="App">
      <section className="top-bar">
        <div>
          <img src="./assets/icons/cloud_icon.svg" alt="cloud_icon" />
          <h2>MyCloud</h2>
        </div>
      </section>

      <section className="main-content">
        <SearchBar />

        <div className="actions">
          <ActionBlock label="Add file" icon="./assets/icons/add_icon.svg" />
          <ActionBlock label="Create folder" icon="./assets/icons/add_icon.svg" />
        </div>

        <Table
          data={data}
          navigateToDir={navigateToDir}
          goBack={goBack}
          deleteFile={deleteFile}
          currDir={dirs} />
      </section>

      <SideBar/>

      
      
{/* 
      <input type="file" onChange={e => {setNewFile(e.target.files[0]); console.log(e.target.files[0])}}/>
      <button onClick={submitNewFile}>Add file</button>  */}

      {/* <input type="text" placeholder="cd ..." onChange={currDirOnChange}/> */}
      {/* <h5>{currDir}</h5>
      <button onClick={() => changeDir(currDir)}>cd ...</button>*/}
    </div>
  );
}

export default App;
