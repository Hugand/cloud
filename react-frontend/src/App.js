import logo from './logo.svg';
import './styles/views/main-page.scss';
import { useEffect, useState } from 'react'
import Table from './components/blocks/Table'
import SideBar from './components/blocks/SideBar'
import ActionBlock from './components/atoms/ActionBlock'
import SearchBar from './components/atoms/SearchBar'

function App() {

  const [ data, setData ] = useState([])
  const [ isConnected, setIsConnected ] = useState(false)
  const [ socket, setSocket ] = useState()
  // const [ currDir, setCurrDir ] = useState("")

  const [ dirs, setDirs ] = useState([])
  const [ prevDir, setPrevDir ] = useState([])

  const [ newFile, setNewFile ] = useState()

  useEffect(() => {
    if (!isConnected) {
      console.log("Connecting...")
      const socket = new WebSocket("ws://localhost:8080/cloud_websocket")

      socket.onopen = () => {
        console.log("Connected to the web socket");
        socket.send(JSON.stringify({
            type: "initialConn",
        }));
        setIsConnected(true)
      };

      socket.onmessage = m => {
        // console.log(JSON.parse(m.data));
        setData(JSON.parse(m.data))
      };

      setSocket(socket)
    }
  }, [])

  function changeDir(dir, _prevDir) {
    if (isConnected) {
      const data = {
          type: "cd",
          directory: './' + dir.join('/'),
          prevDir: './' + _prevDir.join('/')
      }
      console.log(data)
      socket.send(JSON.stringify(data));
      // setPrevDir([...dir])
    }
  }

  function navigateToDir(dirName) {
    // setCurrDir(dirName)
    let newDirStack = [
      ...dirs,
      dirName
    ]
    const tmp = [...dirs]

    setPrevDir(tmp)
    setDirs(newDirStack)
    changeDir(newDirStack, tmp)
  }

  function goBack() {
    let dirStack = [...dirs]
    dirStack.pop()
    const tmp = [...dirs]
    setPrevDir(tmp)
    setDirs(dirStack)
    changeDir(dirStack, tmp)
  }

  const deleteFile = async (fileName) => {
    const fileDirToDelete = `.${dirs.join('/')}/${fileName}`

    let res = await fetch('http://localhost:8080/cloud_files/delete/' + encodeURIComponent(fileDirToDelete), {
      method: "DELETE"
    })

    console.log(res, fileDirToDelete)
  }

  async function submitNewFile(e){
      e.preventDefault()

      let reqBody = new FormData()
      reqBody.append("file", newFile)
      reqBody.append("dir", './' + dirs.join('/'))

      fetch('http://localhost:8080/cloud_files/upload', {
        method: "POST",
        body: reqBody
      })
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
