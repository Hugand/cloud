import { useEffect, useState } from 'react'
import ActionBlock from './components/atoms/ActionBlock'
import SearchBar from './components/atoms/SearchBar'
import Table from './components/blocks/Table'
import SideBar from './components/blocks/SideBar'
import ModalBox from './components/blocks/ModalBox'
import UploadFileBox from './components/blocks/modal_boxes/UploadFileBox'
import API from './helpers/Api';
import './styles/views/main-page.scss';
import { useStateValue } from './state'
import useFileOperations from './hooks/fileOperationsHook'
import Toast from './components/atoms/Toast'

function App() {
  // const [ toast, setToast ] = useState(false)
  const [ data, setData ] = useState([])
  const [ { isConnected, dirs, toast }, dispatch ] = useStateValue()
  const [ displayUploadFileModal, setDisplayUploadFileModal ] = useState(false)

  useEffect(() => {
    console.log(process.env)
    if (!isConnected) {
      const socket = new WebSocket("ws://localhost:8080/cloud_websocket")

      socket.onopen = () => {
        socket.send(JSON.stringify({
            type: "initialConn",
        }));
        dispatch({
          type: 'changeIsConnected',
          value: true
        })
      };

      socket.onmessage = m => {
        setData(JSON.parse(m.data))
      };

      dispatch({
        type: 'changeSocket',
        value: socket
      })
    }
  }, [])

  function resetToast() {
    dispatch({
      type: 'resetToast',
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

      <SideBar/>
      
      <section className="main-content">
        <SearchBar />

        <div className="actions">
          <ActionBlock
            label="Add file"
            icon="./assets/icons/add_icon.svg"
            clickHandler={() => setDisplayUploadFileModal(true)}/>
          <ActionBlock label="Create folder" icon="./assets/icons/add_icon.svg" />
        </div>

        <Table
          data={data} />
      </section>

      <ModalBox
        component={<UploadFileBox
          dirs={dirs}
          handleModalToggle={setDisplayUploadFileModal} />}
        isDisplayed={displayUploadFileModal}
        handleModalToggle={setDisplayUploadFileModal} />
      
      <Toast
        { ...toast }
        resetToast={resetToast} />
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
