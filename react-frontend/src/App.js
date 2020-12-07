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
import MoveFileBox from './components/blocks/modal_boxes/MoveFileBox'
import CreateFolderInput from './components/atoms/CreateFolderInput'

function App() {
  // const [ toast, setToast ] = useState(false)
  const [ data, setData ] = useState([])
  const [ {
    isConnected,
    dirs,
    toast,
    displayMoveFileModal,
    selectedFileActions
  }, dispatch ] = useStateValue()
  const [ displayUploadFileModal, setDisplayUploadFileModal ] = useState(false)
  const [ displayCreateFolderInput, setDisplayCreateFolderInput ] = useState(false)
  const [ searchText, setSearchText ] = useState('')

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

  function handleMoveFileModal(val) {
    dispatch({
      type: 'changeDisplayMoveFileModal',
      value: val
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
        <SearchBar setSearchText={setSearchText}/>

        <div className="actions">
          <ActionBlock
            label="Add file"
            icon="./assets/icons/add_icon.svg"
            clickHandler={() => setDisplayUploadFileModal(true)}/>

          <ActionBlock
            label="Create folder"
            icon="./assets/icons/add_icon.svg"
            clickHandler={() => setDisplayCreateFolderInput(true)}  />

          { displayCreateFolderInput && <CreateFolderInput closeInput={() => setDisplayCreateFolderInput(false)} />}
          

        </div>

        <Table
          data={data}
          searchText={searchText} />
      </section>

      {/* TODO: Remove dirs from UploadFileBox component!!!! */}
      <ModalBox
        component={<UploadFileBox
          dirs={dirs}
          handleModalToggle={setDisplayUploadFileModal} />}
        isDisplayed={displayUploadFileModal}
        handleModalToggle={setDisplayUploadFileModal} />

      <ModalBox
        component={<MoveFileBox
          file={selectedFileActions}
          handleModalToggle={handleMoveFileModal} />}
        isDisplayed={displayMoveFileModal}
        handleModalToggle={handleMoveFileModal} />
      
      <Toast
        { ...toast }
        resetToast={resetToast} />
        
    </div>
  );
}

export default App;
