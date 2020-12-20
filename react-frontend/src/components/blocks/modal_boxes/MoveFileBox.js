import React, { useEffect, useState } from 'react'
import useFileOperations from '../../../hooks/fileOperationsHook'
import { useStateValue } from '../../../state'
import '../../../styles/blocks/move-file-box.scss'

/*
    @props {File} file
    @props {Function} handleModalToggle
*/
function MoveFileBox(props) {
    const dispatch = useStateValue()[1]
    const [ newDirStack, setNewDirStack ] = useState(['.'])
    const [ foldersList, setFoldersList ] = useState([])
    const { getFoldersInDir, moveFile } = useFileOperations()

    useEffect( () => {
        fetchData()
    }, [newDirStack])

    async function fetchData() {
        const foldersList = await getFoldersInDir(newDirStack)
        console.log(foldersList)
        if(foldersList !== null)
            setFoldersList(foldersList)
    }
    
    function goBack() {
        const dir = newDirStack
        if(dir.length > 1) {
            dir.pop()
            setNewDirStack(dir)
            fetchData()
        }
    }

    function changeDir(newDir) {
        const dir = newDirStack
        dir.push(newDir)
        console.log(dir)
        setNewDirStack(dir)
        fetchData()
    }

    async function confirmMoveFile() {
        const status = await moveFile(props.file.file_name, newDirStack)

        if(status) {
            props.handleModalToggle(false)
            dispatch({
                type: 'changeSelectedFileActions',
                value: null
            })
        }
    }
    
    return (
    <>
        <h1 className="modal-title">Move file</h1>
        <h3 className="dark-text instruction">Select a directory to move the file to</h3>
        
        <div className="actions">
                <button onClick={goBack}><img src="./assets/icons/back_arrow_icon.svg" alt="" /></button>
                <p className="dark-text">{newDirStack.join("/")}</p>
            </div>
        { foldersList !== null && 
            <div className="folder-list-container">
                { foldersList.map(f => 
                    <div key={f} className="dark-text folder-row" onClick={() => changeDir(f)}>{ f }</div>) }
                { foldersList.length === 0 && <p className="dark-text no-folders-info">No folders in this directory</p>}
            </div>
        }

        <button className="btn-primary" onClick={confirmMoveFile}>Confirm</button>

    </>)
}

export default MoveFileBox