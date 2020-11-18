import logo from './logo.svg';
import './App.css';
import { useEffect, useState } from 'react'

function App() {

  const [ data, setData ] = useState([])
  const [ isConnected, setIsConnected ] = useState(false)
  const [ socket, setSocket ] = useState()
  // const [ currDir, setCurrDir ] = useState("")

  const [ dirs, setDirs ] = useState([])
  const [ prevDir, setPrevDir ] = useState([])

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

  // const currDirOnChange = (e) => {
  //   setCurrDir(e.target.value)
  // }

  const changeDir = (dir, _prevDir) => {
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


  const navigateToDir = (dirName) => {
    // setCurrDir(dirName)
    let newDirStack = [
      ...dirs,
      dirName
    ]
    const tmp = [...dirs]

    

    console.log(dirs, newDirStack, tmp, dirs === tmp)
    setPrevDir(tmp)
    setDirs(newDirStack)
    changeDir(newDirStack, tmp)
  }

  const goBack = () => {
    let dirStack = [...dirs]
    dirStack.pop()
    const tmp = [...dirs]
    setPrevDir(tmp)
    setDirs(dirStack)
    changeDir(dirStack, tmp)
  }

  const deleteFile = async (fileName) => {
    const fileDirToDelete = `./${dirs.join('/')}/${fileName}`

    let res = await fetch('http://localhost:8080/cloud_files/delete/' + encodeURIComponent(fileDirToDelete), {
      method: "DELETE"
    })

    console.log(res, fileDirToDelete)
  }

  
  return (
    <div className="App">
      <button onClick={goBack}>Back</button>
      
      <table>
        <thead>
          <tr>
            <th>Name</th>
            <th>Size</th>
            <th>Created at</th>
          </tr>
        </thead>
        <tbody>
          {
            data.map(file => <tr>
              { file.type === "file" 
                ? <td>{file.file_name}</td>
                : <td style={{color: "blue"}} onClick={() => navigateToDir(file.file_name)}>{file.file_name}</td>}
              
              <td>{file.file_size}</td>
              <td>{file.file_created_at}</td>
              <td><button onClick={() => deleteFile(file.file_name)}>delete</button></td>
            </tr>)
          }
        </tbody>
      </table>

      {/* <input type="text" placeholder="cd ..." onChange={currDirOnChange}/> */}
      {/* <h5>{currDir}</h5>
      <button onClick={() => changeDir(currDir)}>cd ...</button>
      <button onClick={changeDir}>Add file</button> */}
    </div>
  );
}




export default App;
