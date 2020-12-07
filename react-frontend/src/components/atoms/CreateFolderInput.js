import React, { useState } from 'react'
import useFileOperations from '../../hooks/fileOperationsHook'
import '../../styles/atoms/create-folder-input.scss'

/*
    @props {Function} closeInput
*/
function CreateFolderInput(props) {
    const [ newDirName, setNewDirName ] = useState("")
    const { createDir } = useFileOperations()

    function createNewDir() {
        createDir(newDirName)
        props.closeInput()
    }

    return (
        <div className="create-folder-field-container">
            <input type="text" className="folder-name-field dark-text" onChange={e => setNewDirName(e.target.value)}/>
            <button
                className="close-btn"
                onClick={props.closeInput}>
                    <img src="./assets/icons/cross_icon.svg" alt='' /></button>
            <button 
                className="confirm-btn"
                onClick={createNewDir}>
                    <img src="./assets/icons/tick_icon.svg" alt='' /></button>
        </div>
    )
}

export default CreateFolderInput