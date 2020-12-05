import React, { useState } from 'react'
import API from '../../helpers/Api'
import useFileOperations from '../../hooks/fileOperationsHook'
import '../../styles/atoms/actions-popup.scss'

/*
    @props {File} file
    @props {Function} handlePopupDisplay
*/
function ActionsPopUp(props) {
    const { deleteFile, renameFile } = useFileOperations()
    const [ isRenameActive, setIsRenameActive ] = useState(false)
    const [ newName, setNewName ] = useState("")

    function handleDeleteFile() {
        deleteFile(props.file.file_name)
        props.handlePopupDisplay(-1)
    }

    function handleRenameFile() {
        renameFile(props.file.file_name, newName)
        props.handlePopupDisplay(-1)
    }

    return (
        <div className="actions-popup-container" onClick={e => e.stopPropagation()}>
            {
                !isRenameActive
                ? <button className="dark-text popup-action-btn" onClick={() => setIsRenameActive(true)}>Rename</button>
                : <div className="rename-field-container">
                    <input type="text" className="rename-field" onChange={e => setNewName(e.target.value)}/>
                    <button 
                        className="confirm-btn"
                        onClick={handleRenameFile}>
                            <img src="./assets/icons/tick_icon.svg" alt='' /></button>
                </div>
            }
            
            <button className="dark-text popup-action-btn">Move</button>
            <button className="popup-action-btn delete-btn" onClick={handleDeleteFile}>Delete</button>
        </div>
    )
}

export default ActionsPopUp